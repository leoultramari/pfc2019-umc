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
import java.util.ArrayList;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import shared.model.Funcionario;
import shared.model.TipoMensagem;
import shared.model.dao.FuncionarioDAO;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.Configuracao;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;

/**
 *
 * @author leona
 */
public class ConsumidorFuncionarios {
    
    private static MessageListener consumidorFuncionario = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMessage = (TextMessage) message;

                if(!IntegridadeMensagem.verificarMD5(txtMessage)){
                    return;
                }

                Auditoria.logInfo("Recebeu pacote de funcionários da SME");
                 
                Gson gson = FornecedorGson.getGson();
                JsonArray jsonarray = gson.fromJson(txtMessage.getText(), JsonArray.class);
                
                ArrayList<Funcionario> lista = new ArrayList<>();
                
                for(JsonElement json : jsonarray){
                    JsonObject obj = (JsonObject) json;
                    
                    Funcionario f = gson.fromJson(obj, Funcionario.class);
                    if(f == null){
                        Auditoria.logAviso("Recebeu funcionário corrompido");
                        continue;
                    }              
                    lista.add(f);               
                }

                FuncionarioDAO fDAO = new FuncionarioDAO();
                
                JsonArray idsArray = new JsonArray();
                for(Funcionario f : lista){                 
                    
                    //buscar id local via registro
                    Funcionario fLocal = fDAO.consultarRegistro(f.getRegistro());
                    
                    if(fLocal != null){
                        f.setId(fLocal.getId());
                        fDAO.atualizar(f);
                        Auditoria.logInfo("Atualizando funcionário de registro "+ f.getRegistro() +" com dados da SME");
                    } else {
                        fDAO.cadastrar(f);
                        Auditoria.logInfo("Cadastrando funcionário de registro "+ f.getRegistro() +" da SME");
                    }
                    
                    JsonObject idJson = new JsonObject();
                    idJson.addProperty("registro", f.getRegistro());
                    idsArray.add(idJson);
                }        
                
                Session session = GerenciadorActiveMQ.getSession();
                TextMessage resposta = session.createTextMessage(gson.toJson(idsArray));
                resposta.setIntProperty("tipo", TipoMensagem.ConfirmacaoEnvioFuncionarios.valor);
                resposta.setJMSPriority(7);
                AutenticacaoMensagem.assinar(resposta);
                IntegridadeMensagem.gravarMD5(resposta);
                ProdutorJMS.produzir("sme", resposta);
                Auditoria.logInfo("Confirmação de envio de funcionários enviada para a SME");
                
            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        }

    };

    public static void iniciar() {
        Auditoria.logInfo("Iniciando consumidor de funcionários");

        try {

            String filtro = "tipo = " + TipoMensagem.Funcionarios.valor;
            int idEscola = Configuracao.getInt("autenticacao", "id");
            ConsumidorJMS.consumirFila("escola" + idEscola, consumidorFuncionario, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de funcionários");
            Auditoria.logErro(e);
        }
    }
    
}
