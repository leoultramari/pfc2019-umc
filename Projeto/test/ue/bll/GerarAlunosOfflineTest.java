/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import java.util.List;
import org.junit.Test;
import shared.model.Escola;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.dao.EscolaDAO;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;

/**
 *
 * @author leona
 */
public class GerarAlunosOfflineTest extends GerarInscricoesOnlineTest {

    public void gerarAlunos() throws InterruptedException {

        InscricaoDAO iDAO = new InscricaoDAO();
        EscolaDAO eDAO = new EscolaDAO();

        List<Inscricao> lista = iDAO.listarAbertasCompletas();
        //forçar status das inscrições geradas para RecebidaUE  
        for (Inscricao i : lista) {
            i.setStatus(StatusInscricao.RecebidaUE);
            //alocar para essa escola
            Escola e = eDAO.consultar(Configuracao.getInt("autenticacao", "id"));
            i.setEscolaAlocada(e);
            iDAO.atualizar(i);
        }

        //matricula-las em alunos
        lista = iDAO.listarRecebidasUE();
        for (Inscricao i : lista) {
            if (i.getEscolaAlocada().getId() == Configuracao.getInt("autenticacao", "id")) {
                MatricularAluno.matricular(i.getId());
            }
        }

    }
    
    public int calcularInscricoes() {
        int numSalas = Configuracao.getInt("turmas", "numSalas");
        int capacidadeSala = Configuracao.getInt("turmas", "capacidadeSala");
        int tamanhoMinimo = Configuracao.getInt("turmas", "tamanhoMinimo");
        int numSeries = Configuracao.getInt("turmas", "numSeries");

        Auditoria.logInfo("numSalas - " + numSalas);
        Auditoria.logInfo("capacidadeSala - " + capacidadeSala);
        Auditoria.logInfo("tamanhoMinimo - " + tamanhoMinimo);
        Auditoria.logInfo("numSeries - " + numSeries);

        int pct = (int) Math.round(tamanhoMinimo + (capacidadeSala - tamanhoMinimo) * Math.random());
        return numSeries * numSalas * pct;
    }

    //Gera várias inscrições e força seus status para torna-las em alunos ignorando o ActiveMQ
    @Test
    public void executar() throws InterruptedException {

        gerarInscricoes(calcularInscricoes());
        gerarAlunos();

        //e gerar turmas com esses alunos
        //AlocarAlunos.alocar();

    }

}
