/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import sme.bll.GerenciadorEmail;

/**
 *
 * @author leona
 */
public class TesteEmail {

    public static void main(String[] args) {
        //String corpo = "<html><body><h1> aaa </h1><h2>Lorem ipsum</h2></body></html>";
        String corpoEmail = "<html><body>"
                + "<h1>Caro %pai,</h1>"
                + "<h3>sua pré-inscrição para %nome, realizada na escola %escolaorig foi alocada para a escola</h3>"
                + "<h2>%escolaaloc</h2>"
                + "<h3>Para concluir a matrícula, por favor confirme em pessoa a inscrição na escola alocada.</h3>"
                + "</body></html>";

        String corpoEmailCompleto = corpoEmail;
        corpoEmailCompleto = corpoEmailCompleto.replace("%pai", "Pai");
        corpoEmailCompleto = corpoEmailCompleto.replace("%nome", "Filho");
        corpoEmailCompleto = corpoEmailCompleto.replace("%escolaorig", "Escola 1");
        corpoEmailCompleto = corpoEmailCompleto.replace("%escolaaloc", "Escola 2");

        GerenciadorEmail.enviarEmail("leonardoultramari@gmail.com", "PFC Teste", corpoEmailCompleto);

    }

}
