/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import shared.model.Aluno;
import ue.model.Turma;
import shared.model.dao.AlunoDAO;
import ue.model.dao.TurmaDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;

/**
 *
 * @author leona
 */
public class AlocarAlunos {

    private static int numSalas;
    private static int capacidadeSala;
    private static int tamanhoMinimo;

    private static int numSeries;
    private static int idade1;

    //cálculo de idade baseado no começo do ano atual
    private static int calcularIdade(LocalDate dataNasc) {
        LocalDate dataCalc = LocalDate.of(LocalDate.now().getYear(), 1, 1);
        if (dataNasc != null) {
            return Period.between(dataNasc, dataCalc).getYears();
        } else {
            return 0;
        }
    }

    private static void gerarTurmasParaSerie(ArrayList<Aluno> alunos, int serie, int turmas) {
        Auditoria.logInfo("Realizando alocação de alunos para " + serie + "a série");

        try {

            if (alunos.isEmpty()) {
                Auditoria.logInfo("Nenhum aluno para alocar para " + serie + "a série");
                return;
            }

            //criar turmas
            TurmaDAO tDAO = new TurmaDAO();
            Turma[] listaTurmas = new Turma[turmas + 1];
            for (int i = 1; i <= turmas; i++) {
                Turma t = new Turma();
                t.setAno(LocalDate.now().getYear());
                t.setSerie(serie);

                //ASCII 65 = A
                t.setClasse((char) (i + 64));
                t.setId(tDAO.cadastrar(t));
                listaTurmas[i] = t;
                Auditoria.logInfo("Cadastrou turma de " + serie + "a série " + i);
            }

            AlunoDAO aDAO = new AlunoDAO();
            //iterar pela lista, alocando alunos
            int i = 0;
            for (Aluno a : alunos) {
                i++;
                if (i > turmas) {
                    i = 1;
                }
                Turma t = listaTurmas[i];
                a.setIdTurma(t.getId());
                aDAO.atualizar(a);
                Auditoria.logInfo("Alocou aluno " + a.imprimirId() + " para turma " + t.getId());
            }

            Auditoria.logInfo("Alocação finalizada para " + serie + "a série");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    private static int calcularQtdTurmas(int qtdAlunos) {

        try {

            if (qtdAlunos == 0) {
                Auditoria.logInfo("Turma vazia");
                return 0;
            }

            //Auditoria.logInfo("Calculando turmas para " + qtdAlunos + " alunos");
            Auditoria.logInfo("Tentando formar até " + numSalas + " turmas de tamanho entre " + tamanhoMinimo + " e " + capacidadeSala + " com " + qtdAlunos + " alunos");

            if (qtdAlunos < tamanhoMinimo) {
                Auditoria.logAviso("Existem menos alunos do que o tamanho mínimo de turma para " + qtdAlunos + " alunos");
                return 1;
            }

            if (qtdAlunos > capacidadeSala * numSalas) {
                Auditoria.logAviso("Existem mais alunos do que a escola pode suportar para " + qtdAlunos + " alunos");
                return numSalas;
            }

            //testar para turmas maiores primeiro, minimizando o número de turmas necessárias
            //tentar obter o maior resto possível, nem sempre atenderá ao tamanho mínimo de turma
            int tamanhoDesejado = 0;
            int restoMaximo = 0;
            int turmas = 0;
            int resto = 0;
            for (int i = capacidadeSala; i >= tamanhoMinimo; i--) {
                Auditoria.logDepurar("Testando tamanho de turma " + i + " para " + qtdAlunos);
                turmas = qtdAlunos / i;
                resto = qtdAlunos % i;

                Auditoria.logDepurar("Obteve " + turmas + " turmas completas e " + resto + " alunos restantes para " + qtdAlunos + " alunos");

                //adicionar turma incompleta ao total
                if (resto > 0) {
                    turmas++;
                }

                if (resto == 0) {
                    tamanhoDesejado = i;
                    break;
                }

                if (resto >= restoMaximo) {
                    tamanhoDesejado = i;
                    restoMaximo = resto;
                }
            }

            //Auditoria.logInfo("Obteve " + tamanhoDesejado + " como tamanho ideal para turmas da " + serie + "a série");
            Auditoria.logInfo("Obteve " + turmas + " turmas de tamanho " + tamanhoDesejado + ", resto " + resto);

            if (turmas > numSalas) {
                Auditoria.logInfo("Mais turmas do que a escola pode suportar, ignorando");
                return numSalas;
            }

            return turmas;

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
        return -1;
    }

    /*
    private static void calcularTurmasParaSerie(ArrayList<Aluno> alunos, int serie) {
        Auditoria.logInfo("Realizando alocação de alunos para " + serie + "a série");

        try {

            if (alunos.isEmpty()) {
                Auditoria.logInfo("Nenhum aluno para alocar para " + serie + "a série");
                return;
            }

            int qtdAlunos = alunos.size();

            Auditoria.logInfo("Tentando formar até " + numSalas + " turmas de tamanho entre " + tamanhoMinimo + " e " + capacidadeSala + " com " + qtdAlunos + " alunos");

            if (qtdAlunos < tamanhoMinimo) {
                Auditoria.logAviso("Existem menos alunos do que o tamanho mínimo de turma para " + serie + "a série");
            }

            if (qtdAlunos > capacidadeSala * numSalas) {
                Auditoria.logAviso("Existem mais alunos do que a escola pode suportar para " + serie + "a série");
            }

            //testar para turmas maiores primeiro, minimizando o número de turmas necessárias
            //tentar obter o maior resto possível, nem sempre atenderá ao tamanho mínimo de turma
            int tamanhoDesejado = 0;
            int restoMaximo = 0;
            int turmas = 0;
            int resto = 0;
            for (int i = capacidadeSala; i >= tamanhoMinimo; i--) {
                Auditoria.logDepurar("Testando tamanho de turma " + i + " para " + qtdAlunos);
                turmas = qtdAlunos / i;
                resto = qtdAlunos % i;

                Auditoria.logDepurar("Obteve " + turmas + " turmas completas e " + resto + " alunos restantes para " + serie + "a série");

                //adicionar turma incompleta ao total
                if (resto > 0) {
                    turmas++;
                }

                if (resto == 0) {
                    tamanhoDesejado = i;
                    break;
                }

                if (resto >= restoMaximo) {
                    tamanhoDesejado = i;
                    restoMaximo = resto;
                }
            }

            Auditoria.logInfo("Obteve " + tamanhoDesejado + " como tamanho ideal para turmas da " + serie + "a série");
            Auditoria.logInfo("Obteve " + turmas + " turmas de " + serie + "a série, resto " + resto);

            //criar turmas
            TurmaDAO tDAO = new TurmaDAO();
            Turma[] listaTurmas = new Turma[turmas + 1];
            for (int i = 1; i <= turmas; i++) {
                Turma t = new Turma();
                t.setAno(LocalDate.now().getYear());
                t.setSerie(serie);

                //ASCII 65 = A
                t.setClasse((char) (i + 64));
                t.setId(tDAO.cadastrar(t));
                listaTurmas[i] = t;
                Auditoria.logInfo("Cadastrou turma de " + serie + "a série " + i);
            }

            AlunoDAO aDAO = new AlunoDAO();
            //iterar pela lista, alocando alunos
            int i = 0;
            for (Aluno a : alunos) {
                i++;
                if (i > turmas) {
                    i = 1;
                }
                Turma t = listaTurmas[i];
                a.setIdTurma(t.getId());
                aDAO.atualizar(a);
                Auditoria.logInfo("Alocou aluno " + a.imprimirId() + " para turma " + t.getId());
            }

            Auditoria.logInfo("Alocação finalizada para " + serie + "a série");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }
     */
    public static void alocar() {

        Auditoria.logInfo("Realizando alocação de alunos");

        try {

            AlunoDAO aDAO = new AlunoDAO();
            ArrayList<Aluno> lista = (ArrayList<Aluno>) aDAO.listarNaoAlocados();

            if (lista.isEmpty()) {
                Auditoria.logInfo("Nenhum aluno para alocar");
                return;
            }

            numSalas = Configuracao.getInt("turmas", "numSalas");
            capacidadeSala = Configuracao.getInt("turmas", "capacidadeSala");
            tamanhoMinimo = Configuracao.getInt("turmas", "tamanhoMinimo");

            numSeries = Configuracao.getInt("turmas", "numSeries");
            idade1 = Configuracao.getInt("turmas", "idade1");

            if (numSalas == -1 || capacidadeSala == -1 || tamanhoMinimo == -1) {
                Auditoria.logErro("Configuração inválida, abortando alocação");
                return;
            }

            ArrayList<Aluno> listaTemp = new ArrayList<Aluno>();
            //iterar por série, i=0 para 1a série
            for (int i = 0; i < numSeries; i++) {
                //reiniciar lista, evita problemas com .size() ?
                listaTemp = new ArrayList<Aluno>();
                for (Aluno a : lista) {
                    //idade compatível para essa série
                    if (calcularIdade(a.getDados().getDataNascimento()) == idade1 + i) {
                        //mover para a lista temporária
                        listaTemp.add(a);
                        //remover da lista para economizar processamento
                        //causa erro de comodificação
                        //lista.remove(a);
                    }
                }
                //evitar 0a série
                int qtdTurmas = calcularQtdTurmas(listaTemp.size());
                gerarTurmasParaSerie(listaTemp, i + 1, qtdTurmas);
            }

            Auditoria.logInfo("Alocação de alunos finalizada");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

}
