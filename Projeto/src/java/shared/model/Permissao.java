/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model;

/**
 *
 * @author leona
 */
public class Permissao implements ObjetoDB {

    private int id;
    private TipoPermissao tipoPermissao;
    private Cargo cargo;

    public Permissao() {
    }
    
    public Permissao(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoPermissao getTipoPermissao() {
        return tipoPermissao;
    }

    public void setTipoPermissao(TipoPermissao tipoPermissao) {
        this.tipoPermissao = tipoPermissao;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

}
