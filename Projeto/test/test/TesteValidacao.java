/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import shared.util.Validacao;

/**
 *
 * @author leona
 */
public class TesteValidacao {
    
    public static void main(String[] args) {
        System.out.println(Validacao.validarString("<test> [test2] {test3} &&%%"));
    }
    
}
