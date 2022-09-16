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
public class Escola implements ObjetoDB {

    private int id;
    private String nome;
    //private ArrayList<Bairro> bairros;
    //login e senha para autenticacao em mensagens

    public Escola() {
    }

    public Escola(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    /*
    public ArrayList<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(ArrayList<Bairro> bairros) {
        this.bairros = bairros;
    }
    */
}
