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
import shared.model.TipoDocumento;

/**
 *
 * @author leona
 */
public class TipoDocumentoDAO extends DAOGenerico {
    
    public TipoDocumentoDAO() {
        super();
    }

    public TipoDocumentoDAO(Connection conManual) {
        super(conManual);
    }
    
    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        TipoDocumento objTipado = (TipoDocumento) obj;

        comando.setString(1, objTipado.getNome());
        comando.setBoolean(2, objTipado.isPossuiValidade());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(3, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        TipoDocumento obj = new TipoDocumento(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));
        obj.setPossuiValidade(resultado.getBoolean("possuiValidade"));

        return obj;
    }

    protected static String INSERT = "insert into tipoDocumento (nome, possuiValidade) values(?,?)";
    protected static String UPDATE = "update tipoDocumento set nome = ?, possuiValidade = ? where id = ?";
    protected static String DELETE = "delete from tipoDocumento where id = ?";
    protected static String SELECT_ALL = "select * from tipoDocumento";
    protected static String SELECT = "select * from tipoDocumento where id = ?";

    public int cadastrar(TipoDocumento obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(TipoDocumento obj){
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<TipoDocumento> listar(){
        return (List<TipoDocumento>) listarGenerico(SELECT_ALL);
    }

    public TipoDocumento consultar(int id){
        return (TipoDocumento) consultarGenerico(SELECT, id);
    }
    
}
