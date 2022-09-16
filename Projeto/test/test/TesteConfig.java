/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import shared.util.Configuracao;

/**
 *
 * @author leona
 */
public class TesteConfig {
    
    public static void main(String[] args) {
               
        System.out.println(Configuracao.get("banco", "caminho"));
        System.out.println(Configuracao.get("geral", "contexto"));
        
    }
    
}
