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
public class Filtro implements ObjetoDB {

    private int id;
    private boolean requerLogin = true;
    private Contexto contexto = null;
    private TipoPermissao tipoPermissao = null;
    private String URL;

    public Filtro() {
    }

    public Filtro(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isRequerLogin() {
        return requerLogin;
    }

    public void setRequerLogin(boolean requerLogin) {
        this.requerLogin = requerLogin;
    }

    public Contexto getContexto() {
        return contexto;
    }

    public void setContexto(Contexto contexto) {
        this.contexto = contexto;
    }

    public TipoPermissao getTipoPermissao() {
        return tipoPermissao;
    }

    public void setTipoPermissao(TipoPermissao tipoPermissao) {
        this.tipoPermissao = tipoPermissao;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

}
