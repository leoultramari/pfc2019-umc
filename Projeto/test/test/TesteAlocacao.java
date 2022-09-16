/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import sme.bll.EnviarInscricoesSME;
import sme.bll.AlocarInscricoes;

/**
 *
 * @author leona
 */
public class TesteAlocacao {
    
    public static void main(String[] args) {
        AlocarInscricoes.alocar();
        
        EnviarInscricoesSME.enviar();
    }
    
}
