/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import shared.util.Criptografia;

/**
 *
 * @author leona
 */
public class TesteSalt {

    

    public static void main(String[] args) {
        String salt = Criptografia.gerarSalt();
        Criptografia.hashSenha(salt, Criptografia.SHA256("admin"));
        //Criptografia.hashSenha(salt,"OGM2OTc2ZTViNTQxMDQxNWJkZTkwOGJkNGRlZTE1ZGZiMTY3YTljODczZmM0YmI4YTgxZjZmMmFiNDQ4YTkxOA==");

        salt = Criptografia.gerarSalt();
        Criptografia.hashSenha(salt, Criptografia.SHA256("sme"));
        //Criptografia.hashSenha(salt, "NzdkMDkxNmM2ZDZjODc4YzRmYTlmZDI2MjRmNzA4MDM2MWFiNTI4NDA0ZWE1ZDFmNzI0YmVkMTBlMjdkYWFmMw==");

        salt = Criptografia.gerarSalt();
        Criptografia.hashSenha(salt, Criptografia.SHA256("ue"));
        //Criptografia.hashSenha(salt,"ODVhY2M4MTNjYTNmNDI2ODFkZTUxNDY1Nzg5MWU5N2I2ZWJkN2FhNGJkMjY3M2QyYjVmODI2OTRiNDBjNmM4Zg==");

        salt = Criptografia.gerarSalt();
        Criptografia.hashSenha(salt, Criptografia.SHA256("ue2"));
        //Criptografia.hashSenha(salt,"MjJjNDIxODU1MGE2ZGE1MDkwZGJhNjkxNTkzOGQ2N2YwNGUzNGQzYjM2YmVhYzlkYTRkMjY0YzY4NWE2MTBmNQ==");

        salt = Criptografia.gerarSalt();
        Criptografia.hashSenha(salt, Criptografia.SHA256("ue3"));
        //Criptografia.hashSenha(salt,"OGZjYzAxOTQzNmJiNmE1YTNhZmY1N2VjODVmNGYwZWQ1Y2NhMGQyMzFkY2I3ZGIxNWQ5YTY2ZTdlODAxODlkNA==");
    }

}
