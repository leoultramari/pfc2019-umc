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
import shared.model.Contexto;
import shared.model.ObjetoDB;
import shared.model.Filtro;

/**
 *
 * @author leona
 */
public class FiltroDAO extends DAOGenerico {

    public FiltroDAO() {
        super();
    }

    public FiltroDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Filtro objTipado = (Filtro) obj;

        comando.setBoolean(1, objTipado.isRequerLogin());
        if (objTipado.getContexto() != null) {
            comando.setInt(2, objTipado.getContexto().valor);
        } else {
            comando.setNull(2, Types.INTEGER);
        }
        if (objTipado.getTipoPermissao() != null) {
            comando.setInt(3, objTipado.getTipoPermissao().getId());
        } else {
            comando.setNull(3, Types.INTEGER);
        }
        comando.setString(4, objTipado.getURL());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(5, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Filtro obj = new Filtro(resultado.getInt("id"));

        obj.setRequerLogin(resultado.getBoolean("requerLogin"));
        obj.setContexto(Contexto.fromInteger(resultado.getInt("contexto")));
        obj.setURL(resultado.getString("URL"));

        TipoPermissaoDAO tDAO = new TipoPermissaoDAO();
        obj.setTipoPermissao(tDAO.consultar(resultado.getInt("id_tipoPermissao")));

        return obj;
    }

    protected static String INSERT = "insert into filtro (requerLogin, contexto, id_tipoPermissao, URL) values(?,?,?,?)";
    protected static String UPDATE = "update filtro set requerLogin = ?, contexto = ?, id_tipoPermissao = ?, URL = ? where id = ?";
    protected static String DELETE = "delete from filtro where id = ?";
    protected static String SELECT_ALL = "select * from filtro";
    protected static String SELECT = "select * from filtro where id = ?";

    public int cadastrar(Filtro obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Filtro obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Filtro> listar() {
        return (List<Filtro>) listarGenerico(SELECT_ALL);
    }

    protected static String SELECT_URL_ALL = "select * from filtro where ? like URL";

    public List<Filtro> listarPorURL(String URL) {

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setString(1, "%" + URL + "%");
                } catch (Exception e) {

                }
            }
        };
        return (List<Filtro>) listarGenerico(SELECT_URL_ALL, param);

    }

    public Filtro consultar(int id) {
        return (Filtro) consultarGenerico(SELECT, id);
    }

}
