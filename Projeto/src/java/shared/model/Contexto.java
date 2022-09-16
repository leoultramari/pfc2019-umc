/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import shared.util.Auditoria;

/**
 *
 * @author superalunocmc
 */
public enum Contexto {

    UE(1),
    SME(2);

    public final int valor;

    Contexto(int i) {
        valor = i;
    }
    
    public static Contexto fromString(String str) {

        if(str == null){
            Auditoria.logAviso("Contexto inválido");
            return null;
        }
        
        if (str.equalsIgnoreCase("SME")) {
            return Contexto.SME;
        } else if (str.equalsIgnoreCase("UE")) {
            return Contexto.UE;
        } else {
            return null;
        }

    }
    
    public static Contexto fromInteger(int x) {
        switch (x) {
            case 1:
                return UE;
            case 2:
                return SME;
            default:
                return null;
        }
    }

}
