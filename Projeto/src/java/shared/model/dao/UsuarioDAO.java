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
import java.sql.Types;
import java.util.List;
import shared.model.ObjetoDB;
import shared.model.StatusUsuario;
import shared.model.Usuario;
import shared.util.Auditoria;

/**
 *
 * @author leona
 */
public class UsuarioDAO extends DAOGenerico {

    public UsuarioDAO() {
        super();
    }

    public UsuarioDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Usuario objTipado = (Usuario) obj;

        comando.setString(1, objTipado.getLogin());
        comando.setString(2, objTipado.getSenha());
        comando.setString(3, objTipado.getSalt());
        comando.setInt(4, objTipado.getCargo().getId());
        if (objTipado.getEscola() == null) {
            comando.setNull(5, Types.INTEGER);
        } else {
            comando.setInt(5, objTipado.getEscola().getId());
        }
        comando.setInt(6, objTipado.getStatus().valor);
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(7, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Usuario obj = new Usuario(resultado.getInt("id"));

        obj.setLogin(resultado.getString("login"));
        obj.setSenha(resultado.getString("senha"));
        obj.setSalt(resultado.getString("salt"));

        CargoDAO cDAO = new CargoDAO();
        obj.setCargo(cDAO.consultar(resultado.getInt("id_cargo")));

        if (resultado.getInt("id_escola") != 0) {
            EscolaDAO eDAO = new EscolaDAO();
            obj.setEscola(eDAO.consultar(resultado.getInt("id_escola")));
        }

        obj.setStatus(StatusUsuario.fromInteger(resultado.getInt("status")));

        return obj;
    }

    //Omite a senha
    protected ObjetoDB gerarObjetoSimples(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Usuario obj = new Usuario(resultado.getInt("id"));

        obj.setLogin(resultado.getString("login"));

        CargoDAO cDAO = new CargoDAO();
        obj.setCargo(cDAO.consultar(resultado.getInt("id_cargo")));

        if (resultado.getInt("id_escola") != 0) {
            EscolaDAO eDAO = new EscolaDAO();
            obj.setEscola(eDAO.consultar(resultado.getInt("id_escola")));
        }

        obj.setStatus(StatusUsuario.fromInteger(resultado.getInt("status")));

        return obj;
    }

    protected final static String ATIVO = " status = " + StatusUsuario.Ativo.valor;

    //não inclui senha ou salt
    protected final static String COLUNAS = "id, login, id_cargo, id_escola, status";

    protected static String INSERT = "insert into usuario (login, senha, salt, id_cargo, id_escola, status) values(?,?,?,?,?,?)";
    protected static String UPDATE = "update usuario set login = ?, senha = ?, salt = ?, id_cargo = ?, id_escola = ?, status = ? where id = ?";
    protected static String DELETE = "delete from usuario where id = ?";
    protected static String SELECT_ALL = "select " + COLUNAS + " from usuario where " + ATIVO;
    protected static String SELECT = "select * from usuario where id = ? and " + ATIVO;
    protected static String SELECT_INATIVO = "select * from usuario where id = ? and status = " + StatusUsuario.Inativo.valor;

    public int cadastrar(Usuario obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Usuario obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluirLogico(int id) {
        Usuario u = (Usuario) consultarGenerico(SELECT, id);
        u.setStatus(StatusUsuario.Inativo);
        return atualizar(u);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Usuario> listar() {
        //não inclui senha
        return (List<Usuario>) listarGenerico(SELECT_ALL, null, true);
    }

    public Usuario consultar(int id) {
        //inclui senha
        return (Usuario) consultarGenerico(SELECT, id, true);
    }

    public Usuario consultarInativo(int id) {
        //inclui senha
        return (Usuario) consultarGenerico(SELECT_INATIVO, id, true);
    }

    protected static String SELECT_LOGIN = "select * from usuario where login = ? and " + ATIVO;

    public Usuario consultarPorLogin(String login) {
        ParametrosConsulta param = (PreparedStatement comando) -> {
            try {
                comando.setString(1, login);
            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        };
        return (Usuario) consultarGenerico(SELECT_LOGIN, param);
    }

    //Recebe um usuário com login/senha apenas
    //Retorna um usuário com todos os dados preenchidos, se o login/senha forem válidos
    protected static String SELECT_AUTH = "select * from usuario where login = ? and senha = ? and " + ATIVO;

    public Usuario autenticar(Usuario u) {
        ParametrosConsulta param = (PreparedStatement comando) -> {
            try {
                comando.setString(1, u.getLogin());
                comando.setString(2, u.getSenha());
            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        };
        return (Usuario) consultarGenerico(SELECT_AUTH, param);
    }

}
