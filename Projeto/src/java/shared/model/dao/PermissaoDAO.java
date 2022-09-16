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
import shared.model.Permissao;

/**
 *
 * @author leona
 */
public class PermissaoDAO extends DAOGenerico {

    public PermissaoDAO() {
        super();
    }

    public PermissaoDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Permissao objTipado = (Permissao) obj;

        comando.setInt(1, objTipado.getTipoPermissao().getId());
        comando.setInt(2, objTipado.getCargo().getId());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(3, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Permissao obj = new Permissao(resultado.getInt("id"));

        TipoPermissaoDAO tDAO = new TipoPermissaoDAO();
        obj.setTipoPermissao(tDAO.consultar(resultado.getInt("id_tipoPermissao")));

        CargoDAO cDAO = new CargoDAO();
        obj.setCargo(cDAO.consultar(resultado.getInt("id_cargo")));

        return obj;
    }

    protected static String INSERT = "insert into permissao (id_tipoPermissao, id_cargo) values(?,?)";
    protected static String UPDATE = "update permissao set id_tipoPermissao = ?, id_cargo = ? where id = ?";
    protected static String DELETE = "delete from permissao where id = ?";
    protected static String SELECT_ALL = "select * from permissao";
    protected static String SELECT = "select * from permissao where id = ?";

    public int cadastrar(Permissao obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Permissao obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Permissao> listar() {
        return (List<Permissao>) listarGenerico(SELECT_ALL);
    }

    public Permissao consultar(int id) {
        return (Permissao) consultarGenerico(SELECT, id);
    }
    
    protected static String SELECT_CARGO_ALL = "select * from permissao where id_cargo = ?";

    public List<Permissao> listarPorCargo(int id_cargo){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_cargo);
                } catch (Exception e) {

                }
            }
        };
        return (List<Permissao>) listarGenerico(SELECT_CARGO_ALL, param);

    }
    
    protected static String SELECT_CARGO = "select * from permissao where id_cargo = ? and id_tipoPermissao = ?";

    public Permissao consultarParaCargo(int id_cargo, int id_tipoPermissao){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_cargo);
                    comando.setInt(2, id_tipoPermissao);
                } catch (Exception e) {

                }
            }
        };
        return (Permissao) consultarGenerico(SELECT_CARGO, param);

    }
    
    protected static String SELECT_CARGO_NOME = "select * from permissao p, tipoPermissao t where t.id = p.id_tipoPermissao and id_cargo = ? and t.nome = ?";

    public Permissao consultarParaCargoPorNome(int id_cargo, String nomePermissao){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_cargo);
                    comando.setString(2, nomePermissao);
                } catch (Exception e) {

                }
            }
        };
        return (Permissao) consultarGenerico(SELECT_CARGO_NOME, param);

    }
    

}
