/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.time.LocalDate;

/**
 *
 * @author leona
 */
public class Inscricao implements ObjetoDB {

    //idLocal
    private int id;
    //para identificação junto com escolaOriginal
    private int idInscricao;

    private Escola escolaOriginal;
    private Escola escolaAlocada;
    private LocalDate dataCriada;
    private LocalDate dataEnviada;
    private LocalDate dataRecebida;
    
    private StatusInscricao status;   
    private StatusAluno statusMatricula;
    private DadosAluno dados;

    public Inscricao() {
    }
    
    public String imprimirId(){
        return escolaOriginal.getId() + "-" + idInscricao;
    }

    public Inscricao(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(int idInscricao) {
        this.idInscricao = idInscricao;
    }

    public Escola getEscolaOriginal() {
        return escolaOriginal;
    }

    public void setEscolaOriginal(Escola escolaOriginal) {
        this.escolaOriginal = escolaOriginal;
    }

    public Escola getEscolaAlocada() {
        return escolaAlocada;
    }

    public void setEscolaAlocada(Escola escolaAlocada) {
        this.escolaAlocada = escolaAlocada;
    }

    public DadosAluno getDados() {
        return dados;
    }

    public void setDados(DadosAluno dados) {
        this.dados = dados;
    }

    public LocalDate getDataEnviada() {
        return dataEnviada;
    }

    public void setDataEnviada(LocalDate dataEnviada) {
        this.dataEnviada = dataEnviada;
    }

    public LocalDate getDataRecebida() {
        return dataRecebida;
    }

    public void setDataRecebida(LocalDate dataRecebida) {
        this.dataRecebida = dataRecebida;
    }

    public LocalDate getDataCriada() {
        return dataCriada;
    }

    public void setDataCriada(LocalDate dataCriada) {
        this.dataCriada = dataCriada;
    }

    public StatusInscricao getStatus() {
        return status;
    }

    public void setStatus(StatusInscricao status) {
        this.status = status;
    }

    public StatusAluno getStatusMatricula() {
        return statusMatricula;
    }

    public void setStatusMatricula(StatusAluno statusMatricula) {
        this.statusMatricula = statusMatricula;
    }
    
    

}
