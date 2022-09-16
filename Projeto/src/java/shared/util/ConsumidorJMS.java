/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 *
 * @author superalunocmc
 */
public class ConsumidorJMS {

    public static MessageListener teste = new MessageListener() {

        //cada processo passará seu messageListener
        @Override
        public void onMessage(Message message) {
            try {
                TextMessage txtMsg = (TextMessage) message;
                Auditoria.logDepurar(txtMsg.getText());
            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        }

    };

    public static void consumirFila(String nomeJNDI, MessageListener consumidor) {
        consumirFila(nomeJNDI, consumidor, null);
    }

    public static void consumirFila(String nomeJNDI, MessageListener consumidor, String filtro) {

        if (GerenciadorActiveMQ.isOffline()) {
            return;
        }
        
        try {

            Session session = GerenciadorActiveMQ.getSession();
            Destination fila = (Destination) GerenciadorActiveMQ.getContext().lookup(nomeJNDI);

            MessageConsumer consumer;
            if(filtro != null){
                consumer = session.createConsumer(fila, filtro);
            } else {
                consumer = session.createConsumer(fila);
            }
            
            consumer.setMessageListener(consumidor);

            Auditoria.logDepurar("Consumidor adicionado à fila " + nomeJNDI);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao consumir fila " + nomeJNDI);
            Auditoria.logErro(e);
        }

    }

}
