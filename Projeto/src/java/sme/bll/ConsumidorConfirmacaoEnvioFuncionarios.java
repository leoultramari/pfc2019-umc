/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.time.LocalDate;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import shared.model.Funcionario;
import shared.model.TipoMensagem;
import shared.model.dao.FuncionarioDAO;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.ConsumidorJMS;
import shared.util.FornecedorGson;
import shared.util.IntegridadeMensagem;

/**
 *
 * @author leona
 */
public class ConsumidorConfirmacaoEnvioFuncionarios {
    
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

                FuncionarioDAO fDAO = new FuncionarioDAO();

                String ids = "";
                for (JsonElement json : jsonarray) {
                    JsonObject obj = (JsonObject) json;
      
                    int registro = obj.get("registro").getAsInt();
                    Funcionario f = fDAO.consultarRegistro(registro);
                    if (f == null) {
                        Auditoria.logAviso("Recebeu confirmação de envio de funcionário corrompido");
                        continue;
                    }

                    ids = ids + f.getRegistro() + ", ";

                    f.setDataEnviado(LocalDate.now());
                    fDAO.atualizar(f);
                }

                Auditoria.logInfo("Recebeu confirmação de envio para funcionários " + ids);

            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        }

    };

    public static void iniciar() {
        Auditoria.logInfo("Iniciando consumidor de confirmação de envio de funcionários");

        try {

            String filtro = "tipo = " + TipoMensagem.ConfirmacaoEnvioFuncionarios.valor;
            ConsumidorJMS.consumirFila("sme", consumidorConfirmacaoEnvio, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de confirmação de envio de funcionários");
            Auditoria.logErro(e);
        }
    }
    
}
