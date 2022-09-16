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
import shared.model.Escola;
import shared.model.ObjetoDB;
import shared.util.Auditoria;
import shared.util.ConectaDB;

/**
 *
 * @author superalunocmc
 */
public class EscolaDAO extends DAOGenerico {

    public EscolaDAO() {
        super();
    }

    public EscolaDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Escola objTipado = (Escola) obj;

        comando.setString(1, objTipado.getNome());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(2, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Escola obj = new Escola(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));

        return obj;
    }

    protected static String INSERT = "insert into escola (nome) values(?)";
    protected static String UPDATE = "update escola set nome = ? where id = ?";
    protected static String DELETE = "delete from escola where id = ?";
    protected static String SELECT_ALL = "select * from escola";
    protected static String SELECT = "select * from escola where id = ?";

    public int cadastrar(Escola obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Escola obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Escola> listar() {
        return (List<Escola>) listarGenerico(SELECT_ALL);
    }

    public Escola consultar(int id) {
        return (Escola) consultarGenerico(SELECT, id);
    }

    public int consultarMaiorId() {
        Connection con = null;
        try {
            con = ConectaDB.getConexao();
            PreparedStatement comando = con.prepareStatement("select max(id) from escola");
            ResultSet resultado = comando.executeQuery();

            if (resultado.next()) {
                return resultado.getInt("max");
            }
            con.close();
            return 0;
        } catch (Exception e) {
            Auditoria.logErro(e);
            try {
                con.close();
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            return 0;
        }
    }

}
