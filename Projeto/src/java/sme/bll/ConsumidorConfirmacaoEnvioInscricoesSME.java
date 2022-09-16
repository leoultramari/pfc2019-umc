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
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.TipoMensagem;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.IntegridadeMensagem;

/**
 *
 * @author leona
 */
public class ConsumidorConfirmacaoEnvioInscricoesSME {

    private static MessageListener consumidorConfirmacaoEnvio = new MessageListener() {

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

                Gson gson = FornecedorGson.getGson();
                JsonArray jsonarray = gson.fromJson(txtMessage.getText(), JsonArray.class);

                InscricaoDAO iDAO = new InscricaoDAO();

                String ids = "";
                for (JsonElement json : jsonarray) {
                    JsonObject obj = (JsonObject) json;
      
                    int idEscolaOriginal = obj.get("idEscolaOriginal").getAsInt();
                    int idInscricao = obj.get("idInscricao").getAsInt();
                    Inscricao i = iDAO.consultarUnica(idEscolaOriginal,idInscricao);
                    if (i == null) {
                        Auditoria.logAviso("Recebeu confirmação de envio de inscrição corrompida");
                        continue;
                    }

                    ids = ids + i.imprimirId() + ", ";

                    i.setDataEnviada(LocalDate.now());
                    i.setStatus(StatusInscricao.EnviadaSME);
                    iDAO.atualizar(i);
                }

                Auditoria.logInfo("Recebeu confirmação de envio para inscrições " + ids);

            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        }

    };

    public static void iniciar() {
        Auditoria.logInfo("Iniciando consumidor de confirmação de envio de inscrições");

        try {

            String filtro = "tipo = " + TipoMensagem.ConfirmacaoEnvioInscricoes.valor;
            ConsumidorJMS.consumirFila("sme", consumidorConfirmacaoEnvio, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de confirmação de envio de inscrições");
            Auditoria.logErro(e);
        }
    }

}
