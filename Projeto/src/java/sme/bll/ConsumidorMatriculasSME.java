/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import shared.model.Inscricao;
import shared.model.StatusAluno;
import shared.model.TipoMensagem;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.IntegridadeMensagem;

/**
 *
 * @author leona
 */
public class ConsumidorMatriculasSME {
    
    private static MessageListener consumidorMatriculas = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMessage = (TextMessage) message;
                if (!AutenticacaoMensagem.autenticar(message)) {
                    return;
                }
                if(!IntegridadeMensagem.verificarMD5(txtMessage)){
                    return;
                }

                int idEscola = message.getIntProperty("id");
                 
                Gson gson = FornecedorGson.getGson();
                JsonObject jsonobject = gson.fromJson(txtMessage.getText(), JsonObject.class);
                
                if(jsonobject.get("idInscricao") == null || jsonobject.get("idEscolaOriginal") == null || jsonobject.get("status") == null){
                    Auditoria.logAviso("Recebeu confirmação de matrícula corrompida");
                    return;
                }
                
                int idInscricao = jsonobject.get("idInscricao").getAsInt();
                int idEscolaOriginal = jsonobject.get("idEscolaOriginal").getAsInt();
                StatusAluno statusMatricula = StatusAluno.fromInteger(jsonobject.get("status").getAsInt());
               
                InscricaoDAO iDAO = new InscricaoDAO();
                Inscricao i = iDAO.consultarUnica(idEscolaOriginal, idInscricao);  
                
                if(i == null){
                    Auditoria.logAviso("Recebeu confirmação de matrícula inválida");
                    return;
                }
                
                i.setStatusMatricula(statusMatricula);
                iDAO.atualizar(i);
                
                Auditoria.logInfo("Atualizou inscricao " + i.imprimirId() + " com status de matrícula " + statusMatricula);
                
            } catch (Exception e) {
                Auditoria.logErro(e);
                //reenviar a mensagem para testes
                //ProdutorJMS.produzir("sme", message);
            }
        }

    };

    public static void iniciar() {
        Auditoria.logInfo("Iniciando consumidor de matrículas");

        try {

            String filtro = "tipo = " + TipoMensagem.Matriculas.valor;
            ConsumidorJMS.consumirFila("sme", consumidorMatriculas, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de matrículas");
            Auditoria.logErro(e);
        }
    }
    
}
