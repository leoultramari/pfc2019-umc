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
public class Endereco implements ObjetoDB {

    private int id;
    private String cep;
    private String logradouro;
    private int num;
    private Bairro bairro;
    private DadosAluno dados;

    public Endereco() {
    }

    public Endereco(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public DadosAluno getDados() {
        return dados;
    }

    public void setDados(DadosAluno dados) {
        this.dados = dados;
    }

}
