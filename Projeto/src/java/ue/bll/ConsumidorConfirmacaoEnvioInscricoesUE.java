/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.time.LocalDate;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.TipoMensagem;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.IntegridadeMensagem;

/**
 *
 * @author leona
 */
public class ConsumidorConfirmacaoEnvioInscricoesUE {

    private static MessageListener consumidorConfirmacaoEnvio = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMessage = (TextMessage) message;

                if (!IntegridadeMensagem.verificarMD5(txtMessage)) {
                    return;
                }

                Gson gson = FornecedorGson.getGson();
                JsonArray jsonarray = gson.fromJson(txtMessage.getText(), JsonArray.class);

                InscricaoDAO iDAO = new InscricaoDAO();

                String ids = "";
                for (JsonElement json : jsonarray) {
                    JsonPrimitive prim = (JsonPrimitive) json;

                    int id = prim.getAsInt();
                    Inscricao i = iDAO.consultarIdInscricao(id);
                    if (i == null) {
                        Auditoria.logAviso("Recebeu confirmação de envio de inscrição corrompida");
                        continue;
                    }

                    ids = ids + id + ", ";

                    i.setStatus(StatusInscricao.EnviadaUE);
                    i.setDataEnviada(LocalDate.now());
                    iDAO.atualizar(i);
                    Auditoria.logInfo("Marcando inscrição "+id+" como enviada com sucesso");
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
            ConsumidorJMS.consumirFila("escola" + Configuracao.get("autenticacao", "id"), consumidorConfirmacaoEnvio, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de confirmação de envio de inscrições");
            Auditoria.logErro(e);
        }
    }

}
