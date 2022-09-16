/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import javax.jms.Message;
import javax.jms.Session;
import shared.model.TipoMensagem;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.GerenciadorActiveMQ;
import shared.util.ProdutorJMS;

/**
 *
 * @author superalunocmc
 */
public class VerificacaoPeriodica implements Runnable {

    @Override
    public void run() {

        if (GerenciadorActiveMQ.isOffline()) {
            return;
        }

        try {
            Auditoria.logInfo("Realizando verificação periódica");

            Session session = GerenciadorActiveMQ.getSession();
            Message mensagem = session.createTextMessage("Verificação periódica");

            AutenticacaoMensagem.assinar(mensagem);

            mensagem.setIntProperty("tipo", TipoMensagem.Verificacao.valor);
            mensagem.setJMSPriority(2);
            //mensagem.setJMSExpiration(0);

            ProdutorJMS.produzir("sme", mensagem);

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

}
