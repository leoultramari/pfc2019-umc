/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import shared.util.Auditoria;

/**
 *
 * @author leona
 */
public class TesteAlocacaoAlunos {

    public static void alocar() {

        Auditoria.logInfo("Realizando alocação de alunos");

        try {

            int numSalas = 3;//Configuracao.getInt("turmas", "numSalas");
            int capacidadeSala = 20;//Configuracao.getInt("turmas", "capacidadeSala");
            int tamanhoMinimo = 15;//Configuracao.getInt("turmas", "tamanhoMinimo");

            int qtdAlunos = 14;

            Auditoria.logInfo("Tentando formar até " + numSalas + " turmas de tamanho entre " + tamanhoMinimo + " e " + capacidadeSala + " com " + qtdAlunos + " alunos");

            if (qtdAlunos < tamanhoMinimo) {
                Auditoria.logAviso("Existem menos alunos do que o tamanho mínimo de turma");
            }

            if (qtdAlunos > capacidadeSala * numSalas) {
                Auditoria.logAviso("Existem mais alunos do que a escola pode suportar");
            }

            //testar para turmas maiores primeiro, minimizando o número de turmas necessárias
            //tentar obter o maior resto possível, nem sempre atenderá ao tamanho mínimo de turma
            int tamanhoDesejado = 0;
            int restoMaximo = 0;
            int turmas;
            int resto;
            for (int i = capacidadeSala; i >= tamanhoMinimo; i--) {
                Auditoria.logDepurar("Testando tamanho de turma " + i + " para " + qtdAlunos);
                turmas = qtdAlunos / i;
                resto = qtdAlunos % i;

                Auditoria.logDepurar("Obteve " + turmas + " turmas completas e " + resto + " alunos restantes");

                if (resto == 0) {
                    tamanhoDesejado = i;
                    break;
                }
                
                if (resto >= restoMaximo) {
                    tamanhoDesejado = i;
                    restoMaximo = resto;
                }
            }

            Auditoria.logInfo("Obteve " + tamanhoDesejado + " como tamanho ideal para turmas");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

    public static void main(String[] args) {
        alocar();
    }

}
