/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.InitialContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

/**
 *
 * @author superalunocmc
 */
public class GerenciadorActiveMQ {

    private static InitialContext context;
    private static Connection connection;
    private static Session session;

    //evita timeouts em testes
    private static boolean offline = false;

    public static void iniciar() {
        Auditoria.logInfo("Iniciando conexão ao ActiveMQ");
        try {

            //buscar no JNDI
            context = new InitialContext();
            //ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");

            String endereco = Configuracao.get("activeMQ", "endereco");
            Auditoria.logInfo("Conectando a " + endereco);

            String propriedades = "?wireFormat.maxInactivityDuration=120000";

            ActiveMQConnectionFactory factory = new ActiveMQSslConnectionFactory(endereco + propriedades);

            try {
                connection = factory.createConnection();
                connection.start();

                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                offline = true;
                Auditoria.logErro("ActiveMQ não está em execução");
            }

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    public static void terminar() {

        Auditoria.logInfo("Fechando conexão ao ActiveMQ");
        offline = false;

        try {

            if (session != null) {
                session.close();
                session = null;
            }

            if (connection != null) {
                connection.stop();
                connection.close();
                connection = null;
            }

            if (context != null) {
                context.close();
                context = null;
            }

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

    public static InitialContext getContext() {
        if (offline) {
            return null;
        }
        if (context == null) {
            iniciar();
        }
        return context;
    }

    public static Session getSession() {
        if (offline) {
            return null;
        }
        if (session == null) {
            iniciar();
        }
        return session;
    }

    public static boolean isOffline() {
        return offline;
    }

}
