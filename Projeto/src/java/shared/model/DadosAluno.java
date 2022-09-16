/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author superalunocmc
 */
public class DadosAluno implements ObjetoDB {

    private int id;
    private String nome;
    private char sexo;
    private LocalDate dataNascimento;
    private String filiacao1;
    private String filiacao2;
    private ArrayList<Documento> documentos;
    private ArrayList<Contato> contatos;
    private Endereco endereco;

    public DadosAluno() {
    }

    public DadosAluno(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public ArrayList<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(ArrayList<Documento> documentos) {
        this.documentos = documentos;
    }

    public ArrayList<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(ArrayList<Contato> contatos) {
        this.contatos = contatos;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getFiliacao1() {
        return filiacao1;
    }

    public void setFiliacao1(String filiacao1) {
        this.filiacao1 = filiacao1;
    }

    public String getFiliacao2() {
        return filiacao2;
    }

    public void setFiliacao2(String filiacao2) {
        this.filiacao2 = filiacao2;
    }

}
