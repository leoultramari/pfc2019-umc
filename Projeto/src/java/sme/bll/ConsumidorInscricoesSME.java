/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import shared.util.AutenticacaoMensagem;
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
import shared.model.StatusAluno;
import shared.model.StatusInscricao;
import shared.model.TipoMensagem;
import shared.model.dao.ContatoDAO;
import shared.model.dao.DadosAlunoDAO;
import shared.model.dao.DocumentoDAO;
import shared.model.dao.EnderecoDAO;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;

/**
 *
 * @author superalunocmc
 */
public class ConsumidorInscricoesSME {

    //Consome pacotes de inscrição,
    //cadastrando-as no banco de dados local com adaptação de IDs
    //e responde à escola que enviou o pacote com os IDs de inscrição cadastrados
    private static MessageListener consumidorInscricaoRecebida = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMessage = (TextMessage) message;
                if (!AutenticacaoMensagem.autenticar(message)) {
                    return;
                }
                if (!IntegridadeMensagem.verificarMD5(txtMessage)) {
                    return;
                }

                int idEscola = message.getIntProperty("id");
                Auditoria.logInfo("Recebeu pacote de inscrições da escola " + idEscola);

                Gson gson = FornecedorGson.getGson();
                JsonArray jsonarray = gson.fromJson(txtMessage.getText(), JsonArray.class);

                ArrayList<Inscricao> lista = new ArrayList<>();

                for (JsonElement json : jsonarray) {
                    JsonObject obj = (JsonObject) json;

                    Inscricao i = gson.fromJson(obj, Inscricao.class);
                    if (i == null) {
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
                String ids = "";
                for (Inscricao i : lista) {

                    if (iDAO.consultarUnica(i.getEscolaOriginal().getId(), i.getIdInscricao()) != null) {
                        Auditoria.logAviso("Tentando receber inscrição " + i.imprimirId() + " duplicata, ignorando");
                        continue;
                    }

                    DadosAluno dados = i.getDados();
                    dados.setId(daDAO.cadastrar(dados));

                    for (Contato c : dados.getContatos()) {
                        //System.out.println("contato ID original: " + c.getId());
                        c.setDados(dados);
                        c.setId(cDAO.cadastrar(c));
                        //System.out.println("contato ID novo: " + c.getId());
                    }

                    for (Documento d : dados.getDocumentos()) {
                        d.setDados(dados);
                        d.setId(doDAO.cadastrar(d));
                    }

                    Endereco e = dados.getEndereco();
                    e.setDados(dados);
                    e.setId(eDAO.cadastrar(e));

                    i.setStatus(StatusInscricao.RecebidaSME);
                    i.setStatusMatricula(StatusAluno.Pendente);
                    i.setDataRecebida(LocalDate.now());
                    iDAO.cadastrar(i);

                    idsArray.add(i.getIdInscricao());
                    ids = ids + i.imprimirId() + " ";
                }

                if (!ids.equals("")) {
                    Auditoria.logInfo("Recebeu e cadastrou inscrições " + ids);
                } else {
                    Auditoria.logAviso("Nenhuma inscrição válida recebida");
                }

                Session session = GerenciadorActiveMQ.getSession();
                TextMessage resposta = session.createTextMessage(gson.toJson(idsArray));
                resposta.setIntProperty("tipo", TipoMensagem.ConfirmacaoEnvioInscricoes.valor);
                resposta.setJMSPriority(7);
                IntegridadeMensagem.gravarMD5(resposta);
                ProdutorJMS.produzir("escola" + idEscola, resposta);
                Auditoria.logInfo("Confirmação de envio de inscrições " + ids + " enviada para a escola " + idEscola);

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
            ConsumidorJMS.consumirFila("sme", consumidorInscricaoRecebida, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de inscrições");
            Auditoria.logErro(e);
        }
    }

}
