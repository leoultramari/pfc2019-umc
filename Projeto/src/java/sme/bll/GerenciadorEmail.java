/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import shared.util.Auditoria;
import shared.util.Configuracao;

/**
 *
 * @author leona
 */
public class GerenciadorEmail {
    
    public static void enviarEmail(String destino, String assunto, String corpo) {
        try {
            
            if(!Boolean.parseBoolean(Configuracao.get("email", "ativado"))){
                Auditoria.logDepurar("Tentou enviar email para " + destino + " com assunto " + assunto);
                Auditoria.logInfo("O envio de email foi desativado na configuração.");
                return;
            }
            
            Properties props = System.getProperties();
            
            String from = Configuracao.get("email", "endereco");
            String pass = Configuracao.get("email", "senha");
            String host = Configuracao.get("email", "host");
            String port = Configuracao.get("email", "porta");         

            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            
            Session session = Session.getDefaultInstance(props);
            
            MimeMessage message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress(from));
            
            InternetAddress obj = new InternetAddress(destino);       
            message.addRecipient(Message.RecipientType.TO, obj);
            
            message.setSubject(assunto);
            message.setContent(corpo, "text/html; charset=utf-8");
         
            Transport transport = session.getTransport("smtp");
            transport.connect(host, Integer.parseInt(port), from, pass);       
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            Auditoria.logInfo("Email " + assunto + " foi enviado para " + destino);
            
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
        
    }
    
}
