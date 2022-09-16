/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import shared.model.Contato;
import shared.model.DadosAluno;
import shared.model.Documento;
import shared.model.Endereco;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.TipoMensagem;
import shared.model.dao.ContatoDAO;
import shared.model.dao.DadosAlunoDAO;
import shared.model.dao.DocumentoDAO;
import shared.model.dao.EnderecoDAO;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;
import shared.util.AutenticacaoMensagem;

/**
 *
 * @author leona
 */
public class ConsumidorInscricoesUE {
    
    private static MessageListener consumidorInscricaoRecebida = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMessage = (TextMessage) message;

                if(!IntegridadeMensagem.verificarMD5(txtMessage)){
                    return;
                }

                Auditoria.logInfo("Recebeu pacote de inscrições da SME");
                 
                Gson gson = FornecedorGson.getGson();
                JsonArray jsonarray = gson.fromJson(txtMessage.getText(), JsonArray.class);
                
                ArrayList<Inscricao> lista = new ArrayList<>();
                
                for(JsonElement json : jsonarray){
                    JsonObject obj = (JsonObject) json;
                    
                    Inscricao i = gson.fromJson(obj, Inscricao.class);
                    if(i == null){
                        Auditoria.logAviso("Recebeu inscrição corrompida");
                        continue;
                    }              
                    lista.add(i);               
                }
                
                InscricaoDAO iDAO = new InscricaoDAO();
                
                DadosAlunoDAO daDAO = new DadosAlunoDAO();
                ContatoDAO cDAO = new ContatoDAO();
                DocumentoDAO doDAO = new DocumentoDAO();
                EnderecoDAO eDAO = new EnderecoDAO();
                
                JsonArray idsArray = new JsonArray();
                for(Inscricao i : lista){
                    
                    DadosAluno dados = i.getDados();                    
                    dados.setId(daDAO.cadastrar(dados));
                    
                    for(Contato c : dados.getContatos()){
                        //System.out.println("contato ID original: " + c.getId());
                        c.setDados(dados);
                        c.setId(cDAO.cadastrar(c));     
                        //System.out.println("contato ID novo: " + c.getId());
                    }
                    
                    for(Documento d : dados.getDocumentos()){
                        d.setDados(dados);
                        d.setId(doDAO.cadastrar(d));                       
                    }
                    
                    Endereco e = dados.getEndereco();    
                    e.setDados(dados);
                    e.setId(eDAO.cadastrar(e));
                    
                    int idEscola = Configuracao.getInt("autenticacao", "id");
                    
                    i.setStatus(StatusInscricao.RecebidaUE);
                    
                    if(i.getEscolaOriginal().getId() == idEscola){
                        //buscar id local via id inscricao
                        Inscricao inscricaoLocal = iDAO.consultarIdInscricao(i.getIdInscricao());
                        i.setId(inscricaoLocal.getId());
                        //esta data existe apenas localmente
                        i.setDataEnviada(inscricaoLocal.getDataEnviada());
                        //data quando recebemos a alocação da SME - agora
                        i.setDataRecebida(LocalDate.now());
                        iDAO.atualizar(i);
                        Auditoria.logInfo("Atualizando inscrição "+ i.imprimirId() +" com alocação da SME");
                    } else if(i.getEscolaAlocada().getId() == idEscola){
                        iDAO.cadastrar(i);
                        Auditoria.logInfo("Recebendo inscrição "+ i.imprimirId() +" da SME");
                    }
                    
                    JsonObject idJson = new JsonObject();
                    idJson.addProperty("idEscolaOriginal", i.getEscolaOriginal().getId());
                    idJson.addProperty("idInscricao", i.getIdInscricao());
                    idsArray.add(idJson);
                }        
                
                Session session = GerenciadorActiveMQ.getSession();
                TextMessage resposta = session.createTextMessage(gson.toJson(idsArray));
                resposta.setIntProperty("tipo", TipoMensagem.ConfirmacaoEnvioInscricoes.valor);
                resposta.setJMSPriority(7);
                AutenticacaoMensagem.assinar(resposta);
                IntegridadeMensagem.gravarMD5(resposta);
                ProdutorJMS.produzir("sme", resposta);
                Auditoria.logInfo("Confirmação de envio de inscrições enviada para a SME");
                
            } catch (Exception e) {
                Auditoria.logErro(e);
                //reenviar a mensagem para testes
                //ProdutorJMS.produzir("sme", message);
            }
        }

    };

    public static void iniciar() {
        Auditoria.logInfo("Iniciando consumidor de inscrições");

        try {

            String filtro = "tipo = " + TipoMensagem.Inscricoes.valor;
            int idEscola = Configuracao.getInt("autenticacao", "id");
            ConsumidorJMS.consumirFila("escola" + idEscola, consumidorInscricaoRecebida, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de inscrições");
            Auditoria.logErro(e);
        }
    }
    
}
