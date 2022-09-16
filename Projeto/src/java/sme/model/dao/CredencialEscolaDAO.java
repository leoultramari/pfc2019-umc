/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import shared.model.ObjetoDB;
import shared.model.dao.DAOGenerico;
import shared.model.dao.EscolaDAO;
import shared.model.dao.ParametrosConsulta;
import sme.model.CredencialEscola;

/**
 *
 * @author superalunocmc
 */
public class CredencialEscolaDAO extends DAOGenerico {

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        CredencialEscola objTipado = (CredencialEscola) obj;

        comando.setInt(1, objTipado.getEscola().getId());
        comando.setString(2, objTipado.getLogin());
        comando.setString(3, objTipado.getSenha());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(4, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        CredencialEscola obj = new CredencialEscola(resultado.getInt("id"));

        obj.setLogin(resultado.getString("login"));
        obj.setSenha(resultado.getString("senha"));
        
        EscolaDAO eDAO = new EscolaDAO();
        obj.setEscola(eDAO.consultar(resultado.getInt("id_escola")));
        
        return obj;
    }

    protected static String INSERT = "insert into credencialEscola (id_escola, login, senha) values(?,?,?)";
    protected static String UPDATE = "update credencialEscola set id_escola = ?, login = ?, senha = ? where id = ?";
    protected static String DELETE = "delete from credencialEscola where id = ?";
    protected static String SELECT_ALL = "select * from credencialEscola";
    protected static String SELECT = "select * from credencialEscola where id = ?";

    public int cadastrar(CredencialEscola obj) throws ClassNotFoundException, SQLException {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(CredencialEscola obj) throws ClassNotFoundException, SQLException {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) throws ClassNotFoundException, SQLException {
        return excluirGenerico(id, DELETE);
    }

    public List<CredencialEscola> listar() throws ClassNotFoundException, SQLException {
        return (List<CredencialEscola>) listarGenerico(SELECT_ALL);
    }

    public CredencialEscola consultar(int id) throws ClassNotFoundException, SQLException {
        return (CredencialEscola) consultarGenerico(SELECT, id);
    }
    
    //Recebe uma credencial com login/senha apenas
    //Retorna uma credencial com todos os dados preenchidos, se o login/senha forem válidos
    //Necessário para verificar se a credencial corresponde à mesma escola que está pedindo autenticação
    protected static String SELECT_AUTH = "select * from credencialEscola where login = ? and senha = ?";
    public CredencialEscola autenticar(CredencialEscola c) throws ClassNotFoundException, SQLException {
        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setString(1, c.getLogin());
                    comando.setString(2, c.getSenha());
                } catch (Exception e) {

                }
            }
        };
        return (CredencialEscola) consultarGenerico(SELECT_AUTH, param);
    }

}
