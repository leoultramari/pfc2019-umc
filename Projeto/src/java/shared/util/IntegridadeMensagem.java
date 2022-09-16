/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import javax.jms.TextMessage;

/**
 *
 * @author leona
 */
public class IntegridadeMensagem {

    private static String gerarMD5(String text) {
        try {
            byte[] bytesOfMessage = text.getBytes("UTF-8");

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5bytes = md.digest(bytesOfMessage);

            return String.format("%032x", new BigInteger(1, md5bytes));

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
        return null;
    }

    public static void gravarMD5(TextMessage message) {
        try {
            String md5 = gerarMD5(message.getText());
            Auditoria.logDepurar2("Gerou MD5 " + md5 + " para a mensagem");

            message.setStringProperty("md5", md5);

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    public static boolean verificarMD5(TextMessage message) {
        try {

            if (!message.propertyExists("md5")) {
                Auditoria.logAviso("Verificando mensagem sem MD5");
                return false;
            }

            String md5Recebido = message.getStringProperty("md5");
            String md5Corpo = gerarMD5(message.getText());
            
            if (!md5Recebido.equals(md5Corpo)) {
                Auditoria.logAviso("MD5 inválido, mensagem corrompida");
                return false;
            } else {
                Auditoria.logDepurar2("MD5 verificado");
                return true;
            }
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
        return false;
    }

}
