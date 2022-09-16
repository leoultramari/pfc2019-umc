/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author superalunocmc
 */
public class Aluno implements ObjetoDB {

    private int id;
    private int rm;
    private StatusAluno status;
    private DadosAluno dados;

    private int idInscricao;
    private int idEscolaOriginal;

    private int idTurma;

    public Aluno() {
    }

    public String imprimirId() {
        return idEscolaOriginal + "-" + idInscricao;
    }

    public Aluno(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRm() {
        return rm;
    }

    public void setRm(int rm) {
        this.rm = rm;
    }

    public StatusAluno getStatus() {
        return status;
    }

    public void setStatus(StatusAluno status) {
        this.status = status;
    }

    public DadosAluno getDados() {
        return dados;
    }

    public void setDados(DadosAluno dados) {
        this.dados = dados;
    }

    public int getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(int idInscricao) {
        this.idInscricao = idInscricao;
    }

    public int getIdEscolaOriginal() {
        return idEscolaOriginal;
    }

    public void setIdEscolaOriginal(int idEscolaOriginal) {
        this.idEscolaOriginal = idEscolaOriginal;
    }

    public int getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(int idTurma) {
        this.idTurma = idTurma;
    }

}
