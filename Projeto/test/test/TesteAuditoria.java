/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import shared.util.Auditoria;

/**
 *
 * @author alunocmc
 */
public class TesteAuditoria {
    
    public static void main(String[] args) {
        
        System.out.println(System.getProperty("java.util.logging.SimpleFormatter.format"));
        
        Auditoria.iniciar();
        
        Auditoria.logInfo("aaaaa");
        Auditoria.logAviso("bbbbbbb");
        
        System.out.println(System.getProperty("java.util.logging.SimpleFormatter.format"));
    }
    
}
