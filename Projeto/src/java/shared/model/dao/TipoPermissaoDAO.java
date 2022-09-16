/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import shared.model.ObjetoDB;
import shared.model.TipoPermissao;

/**
 *
 * @author leona
 */
public class TipoPermissaoDAO extends DAOGenerico {
    
    public TipoPermissaoDAO() {
        super();
    }

    public TipoPermissaoDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        TipoPermissao objTipado = (TipoPermissao) obj;

        comando.setString(1, objTipado.getNome());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(2, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        TipoPermissao obj = new TipoPermissao(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));

        return obj;
    }

    protected static String INSERT = "insert into tipoPermissao (nome) values(?)";
    protected static String UPDATE = "update tipoPermissao set nome = ? where id = ?";
    protected static String DELETE = "delete from tipoPermissao where id = ?";
    protected static String SELECT_ALL = "select * from tipoPermissao";
    protected static String SELECT = "select * from tipoPermissao where id = ?";

    public int cadastrar(TipoPermissao obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(TipoPermissao obj){
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<TipoPermissao> listar(){
        return (List<TipoPermissao>) listarGenerico(SELECT_ALL);
    }

    public TipoPermissao consultar(int id){
        return (TipoPermissao) consultarGenerico(SELECT, id);
    }
    
    protected static String SELECT_NOME = "select * from tipoPermissao where nome like ?";

    public TipoPermissao consultarPorNome(String nome){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setString(1, nome);
                } catch (Exception e) {

                }
            }
        };
        return (TipoPermissao) consultarGenerico(SELECT_NOME, param);

    }

}
