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
import shared.model.TipoContato;
import shared.model.ObjetoDB;

/**
 *
 * @author leona
 */
public class TipoContatoDAO extends DAOGenerico {
    
    public TipoContatoDAO() {
        super();
    }

    public TipoContatoDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        TipoContato objTipado = (TipoContato) obj;

        comando.setString(1, objTipado.getNome());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(2, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        TipoContato obj = new TipoContato(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));

        return obj;
    }

    protected static String INSERT = "insert into tipoContato (nome) values(?)";
    protected static String UPDATE = "update tipoContato set nome = ? where id = ?";
    protected static String DELETE = "delete from tipoContato where id = ?";
    protected static String SELECT_ALL = "select * from tipoContato";
    protected static String SELECT = "select * from tipoContato where id = ?";

    public int cadastrar(TipoContato obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(TipoContato obj){
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<TipoContato> listar(){
        return (List<TipoContato>) listarGenerico(SELECT_ALL);
    }

    public TipoContato consultar(int id){
        return (TipoContato) consultarGenerico(SELECT, id);
    }

}
