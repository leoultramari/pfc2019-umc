/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author leona
 */
public enum StatusInscricao {
    
    Desconhecido(-1),
    Aberta(0),      //para UE
    EnviadaUE(1),   //para UE
    RecebidaSME(2), //para SME
    Alocada(3),     //para SME
    EnviadaSME(4),  //para SME
    RecebidaUE(5),  //para UE
    Cancelada(6),   //para ambos
    Completa(7),    //para UE
    Arquivada(8);   //para SME
    
    public final int valor;

    StatusInscricao(int i) {
        valor = i;
    }
    
    
    //soluciona fromInteger
    private static Map<Integer, StatusInscricao> map = new HashMap<Integer, StatusInscricao>();

    static {
        for (StatusInscricao valorEnum : StatusInscricao.values()) {
            map.put(valorEnum.valor, valorEnum);
        }
    }

    public static StatusInscricao fromInteger(int valor) {
        return map.get(valor);
    }
    
}
