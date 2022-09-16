/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 *
 * @author leona
 */
public class Criptografia {

    //criptografar com SHA256 e codificar em base64
    public static String SHA256(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashSenha = digest.digest(
                    senha.getBytes(StandardCharsets.UTF_8));
            String senhaHashed = Base64.getEncoder().encodeToString(hashSenha);
            return senhaHashed;
        } catch (Exception e) {
            Auditoria.logErro(e);
            return null;
        }
    }

    public static String gerarSalt() {
        try {
            //gerar 16 bytes de salt
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);

            //converter salt para hexadecimal
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < salt.length; i++) {
                sb.append(Integer.toString((salt[i] & 0xff) + 0x100, 16).substring(1));
            }
            //converter para string
            //String saltString = Base64.getEncoder().encodeToString(salt);
            String saltString = sb.toString();
            Auditoria.logDepurar3("Gerou salt " + saltString);
            return saltString;
        } catch (Exception e) {
            Auditoria.logErro(e);
            return null;
        }
    }

    //adicionar salt a senha e criptografar
    public static String hashSenha(String salt, String senha) {
        try {
            String senhaSalted = salt + senha;
            String senhaHashed = SHA256(senhaSalted);
            Auditoria.logDepurar2("Usou salt " + salt + " com senha " + senha + " e criptografou para " + senhaHashed);
            return senhaHashed;
        } catch (Exception e) {
            Auditoria.logErro(e);
            return null;
        }
    }

}
