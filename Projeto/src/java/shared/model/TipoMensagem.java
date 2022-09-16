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
public enum TipoMensagem {

    Desconhecido(-1),
    Comum(0),
    Verificacao(1),
    Inscricoes(2),
    ConfirmacaoEnvioInscricoes(3),
    Erro(4),
    Matriculas(5),
    Funcionarios(6),
    ConfirmacaoEnvioFuncionarios(7);

    public final int valor;

    TipoMensagem(int i) {
        valor = i;
    }
    
    //soluciona fromInteger
    private static Map<Integer, TipoMensagem> map = new HashMap<Integer, TipoMensagem>();

    static {
        for (TipoMensagem valorEnum : TipoMensagem.values()) {
            map.put(valorEnum.valor, valorEnum);
        }
    }

    public static TipoMensagem fromInteger(int valor) {
        return map.get(valor);
    }

    
}
