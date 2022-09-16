/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.model;

import shared.model.ObjetoDB;

/**
 *
 * @author leona
 */
public class Turma implements ObjetoDB {

    private int id;
    private int serie;
    private char classe;
    private int ano;

    //preencher apenas para exibir no frontend
    private int tamanho;

    public Turma() {
    }

    public Turma(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public char getClasse() {
        return classe;
    }

    public void setClasse(char classe) {
        this.classe = classe;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public String imprimirTurma() {
        return serie + "-" + classe;
    }

}
