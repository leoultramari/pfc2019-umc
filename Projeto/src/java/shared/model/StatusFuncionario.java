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
public enum StatusFuncionario {
    
    Desconhecido(-1),
    Ativo(0),
    Inativo(1);
    
    public final int valor;

    StatusFuncionario(int i) {
        valor = i;
    }
    
    
    //soluciona fromInteger
    private static Map<Integer, StatusFuncionario> map = new HashMap<Integer, StatusFuncionario>();

    static {
        for (StatusFuncionario valorEnum : StatusFuncionario.values()) {
            map.put(valorEnum.valor, valorEnum);
        }
    }

    public static StatusFuncionario fromInteger(int valor) {
        return map.get(valor);
    }
    
}
