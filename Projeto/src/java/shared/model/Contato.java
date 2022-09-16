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
public class Contato implements ObjetoDB {

    private int id;
    private TipoContato tipoContato;
    private String dado;
    private DadosAluno dados;

    public Contato() {
    }

    public Contato(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoContato getTipoContato() {
        return tipoContato;
    }

    public void setTipoContato(TipoContato tipoContato) {
        this.tipoContato = tipoContato;
    }

    public String getDado() {
        return dado;
    }

    public void setDado(String dado) {
        this.dado = dado;
    }

    public DadosAluno getDados() {
        return dados;
    }

    public void setDados(DadosAluno dados) {
        this.dados = dados;
    }

}
