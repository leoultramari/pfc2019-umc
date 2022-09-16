/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

/**
 *
 * @author superalunocmc
 */
public class ProdutorJMS {

    public static void produzirTexto(String nomeJNDI, String text) {
        try {
            Session session = GerenciadorActiveMQ.getSession();
            Message message = session.createTextMessage(text);
            produzir(nomeJNDI, message);
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    public static void produzir(String nomeJNDI, Message message) {

        if (GerenciadorActiveMQ.isOffline()) {
            return;
        }

        try {

            Session session = GerenciadorActiveMQ.getSession();
            Destination fila = (Destination) GerenciadorActiveMQ.getContext().lookup(nomeJNDI);

            MessageProducer producer = session.createProducer(fila);

            producer.setPriority(message.getJMSPriority());
            producer.send(message);

            Auditoria.logDepurar2("Mensagem enviada na fila " + nomeJNDI);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao enviar mensagem para fila " + nomeJNDI);
            Auditoria.logErro(e);
        }

    }

}
