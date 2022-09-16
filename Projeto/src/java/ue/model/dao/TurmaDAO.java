/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import shared.model.ObjetoDB;
import ue.model.Turma;
import shared.model.dao.DAOGenerico;
import shared.model.dao.ParametrosConsulta;

/**
 *
 * @author leona
 */
public class TurmaDAO extends DAOGenerico {

    public TurmaDAO() {
        super();
    }

    public TurmaDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Turma objTipado = (Turma) obj;

        comando.setInt(1, objTipado.getSerie());
        comando.setString(2, String.valueOf(objTipado.getClasse()));
        comando.setInt(3, objTipado.getAno());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(4, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Turma obj = new Turma(resultado.getInt("id"));

        obj.setSerie(resultado.getInt("serie"));
        obj.setClasse(resultado.getString("classe").charAt(0));
        obj.setAno(resultado.getInt("ano"));

        return obj;
    }

    protected static String INSERT = "insert into turma (serie, classe, ano) values(?,?,?)";
    protected static String UPDATE = "update turma set serie = ?, classe = ?, ano = ? where id = ?";
    protected static String DELETE = "delete from turma where id = ?";
    protected static String SELECT_ALL = "select * from turma";
    protected static String SELECT = "select * from turma where id = ?";

    public int cadastrar(Turma obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Turma obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Turma> listar() {
        return (List<Turma>) listarGenerico(SELECT_ALL);
    }

    public Turma consultar(int id) {
        return (Turma) consultarGenerico(SELECT, id);
    }

    protected static String SELECT_SERIE = "select * from turma where serie = ?";

    public Turma consultarPorSerie(int serie) {

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, serie);
                } catch (Exception e) {

                }
            }
        };
        return (Turma) consultarGenerico(SELECT_SERIE, param);

    }
}
