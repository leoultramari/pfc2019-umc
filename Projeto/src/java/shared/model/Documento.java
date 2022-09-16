/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

import java.time.LocalDate;

/**
 *
 * @author superalunocmc
 */
public class Documento implements ObjetoDB {

    private int id;
    private TipoDocumento tipoDocumento;
    private String dado;
    private LocalDate validade;
    private DadosAluno dados;

    public Documento() {
    }

    public Documento(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    public DadosAluno getDados() {
        return dados;
    }

    public void setDados(DadosAluno dados) {
        this.dados = dados;
    }

}
