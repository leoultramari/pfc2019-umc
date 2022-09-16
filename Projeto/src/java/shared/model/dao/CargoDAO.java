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
import shared.model.Cargo;

/**
 *
 * @author leona
 */
public class CargoDAO extends DAOGenerico{
    
    public CargoDAO() {
        super();
    }

    public CargoDAO(Connection conManual) {
        super(conManual);
    }
    
    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Cargo objTipado = (Cargo) obj;

        comando.setString(1, objTipado.getNome());

    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(2, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Cargo obj = new Cargo(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));

        return obj;
    }

    protected static String INSERT = "insert into cargo(nome) values(?)";
    protected static String UPDATE = "update cargo set nome = ? where id = ?";
    protected static String DELETE = "delete from cargo where id = ?";
    protected static String SELECT_ALL = "select * from cargo";
    protected static String SELECT = "select * from cargo where id = ?";

    public int cadastrar(Cargo obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Cargo obj){
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<Cargo> listar(){
        return (List<Cargo>) listarGenerico(SELECT_ALL);
    }

    public Cargo consultar(int id){
        return (Cargo) consultarGenerico(SELECT, id);
    }
    
}
