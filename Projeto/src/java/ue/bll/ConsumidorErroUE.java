/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import shared.model.TipoMensagem;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.ConsumidorJMS;

/**
 *
 * @author leona
 */
public class ConsumidorErroUE {

    private static boolean possuiErro = false;
    private static String erro = "";

    private static MessageListener consumidorErro = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMessage = (TextMessage) message;

                if (txtMessage.getText() == null) {
                    possuiErro = false;
                    erro = "";
                    //System.out.println("Recebeu fim do erro da SME");
                } else {
                    possuiErro = true;
                    erro = txtMessage.getText();
                    Auditoria.logErro("Recebeu erro da SME - " + erro);
                }

            } catch (Exception e) {
                Auditoria.logErro(e);
                //reenviar a mensagem para testes
                //ProdutorJMS.produzir("sme", message);
            }
        }

    };

    public static boolean isPossuiErro() {
        return possuiErro;
    }

    public static String getErro() {
        return erro;
    }

    public static void iniciar() {
        Auditoria.logInfo("Iniciando consumidor de erros");

        try {

            String filtro = "tipo = " + TipoMensagem.Erro.valor;
            int idEscola = Configuracao.getInt("autenticacao", "id");
            ConsumidorJMS.consumirFila("escola" + idEscola, consumidorErro, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar consumidor de erros");
            Auditoria.logErro(e);
        }
    }

}
