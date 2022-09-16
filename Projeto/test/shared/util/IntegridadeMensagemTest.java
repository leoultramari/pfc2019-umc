/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alunocmc
 */
public class IntegridadeMensagemTest {

    //verifica se a classe consegue gerar e verificar MD5s corretos
    @Test
    public void VerificarGeracaoMD5() throws JMSException {

        GerenciadorActiveMQ.iniciar();

        Session session = GerenciadorActiveMQ.getSession();
        TextMessage mensagem = session.createTextMessage("teste");

        IntegridadeMensagem.gravarMD5(mensagem);

        assertEquals(mensagem.getStringProperty("md5"), "698dc19d489c4e4db73e28a713eab07b");

        assertEquals(IntegridadeMensagem.verificarMD5(mensagem), true);
    }

}
