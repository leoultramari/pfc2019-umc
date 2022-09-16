/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.model;

import shared.model.Escola;
import shared.model.ObjetoDB;

/**
 *
 * @author superalunocmc
 */
public class CredencialEscola implements ObjetoDB {

    private int id;
    private Escola escola;
    private String login;
    private String senha;

    public CredencialEscola() {
    }

    public CredencialEscola(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
