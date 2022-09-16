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
 * @author superalunocmc
 */
public enum StatusAluno {
    
    Desconhecido(0),
    Matriculado(1),
    Pendente(2),
    Cancelado(3);
    
    public final int valor;

    StatusAluno(int i) {
        valor = i;
    }

   //soluciona fromInteger
    private static Map<Integer, StatusAluno> map = new HashMap<Integer, StatusAluno>();

    static {
        for (StatusAluno valorEnum : StatusAluno.values()) {
            map.put(valorEnum.valor, valorEnum);
        }
    }

    public static StatusAluno fromInteger(int valor) {
        return map.get(valor);
    }
    
}
