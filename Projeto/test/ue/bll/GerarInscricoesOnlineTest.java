/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import org.junit.Test;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.GeradorObjetosTeste;

/**
 *
 * @author leona
 */
public class GerarInscricoesOnlineTest {

    public void gerarInscricoes(int qtd) throws InterruptedException {

        Auditoria.iniciar();
        Auditoria.logInfo("Gerando " + qtd + " inscrições");

        //gerar várias inscrições
        for (int i = 0; i < qtd; i++) {
            Auditoria.logInfo("Gerando inscrição " + i);
            GeradorObjetosTeste.CadastrarInscricaoTeste();
        }

    }

    //Apenas gera várias inscrições, sem forçar seu status
    @Test
    public void executar() throws InterruptedException {

        int qtd = Configuracao.getInt("teste", "qtd");
        gerarInscricoes(qtd);

    }

}
