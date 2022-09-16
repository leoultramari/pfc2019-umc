/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import org.junit.Test;
import static org.junit.Assert.*;
import shared.model.Contexto;

/**
 *
 * @author alunocmc
 */
public class InicializacaoTest {

    @Test
    private void TestarInicializacaoContextoAtual() {

        //verificar se as tarefas relevantes ao contexto atual foram realizadas
        if (Configuracao.getContextoAtual() == Contexto.SME) {

        } else if (Configuracao.getContextoAtual() == Contexto.UE) {

        }

    }

}
