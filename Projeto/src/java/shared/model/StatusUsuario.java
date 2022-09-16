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
public enum StatusUsuario {
    
    Desconhecido(-1),
    Ativo(0),
    Inativo(1);
    
    public final int valor;

    StatusUsuario(int i) {
        valor = i;
    }
    
    
    //soluciona fromInteger
    private static Map<Integer, StatusUsuario> map = new HashMap<Integer, StatusUsuario>();

    static {
        for (StatusUsuario valorEnum : StatusUsuario.values()) {
            map.put(valorEnum.valor, valorEnum);
        }
    }

    public static StatusUsuario fromInteger(int valor) {
        return map.get(valor);
    }
    
}
