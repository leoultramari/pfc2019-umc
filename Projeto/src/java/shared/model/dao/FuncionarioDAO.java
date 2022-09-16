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
import java.time.LocalDate;
import java.util.List;
import shared.model.ObjetoDB;
import shared.model.StatusFuncionario;
import shared.model.Funcionario;

/**
 *
 * @author leona
 */
public class FuncionarioDAO extends DAOGenerico {

    public FuncionarioDAO() {
        super();
    }

    public FuncionarioDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Funcionario objTipado = (Funcionario) obj;

        comando.setInt(1, objTipado.getRegistro());
        comando.setString(2, objTipado.getNome());
        comando.setString(3, objTipado.getEmail());
        comando.setString(4, objTipado.getTelefone());
        comando.setDate(5, java.sql.Date.valueOf(objTipado.getDataInicio()));

        if (objTipado.getDataSaida() == null) {
            comando.setNull(6, Types.DATE);
        } else {
            comando.setDate(6, java.sql.Date.valueOf(objTipado.getDataSaida()));
        }

        comando.setInt(7, objTipado.getEscola().getId());

        comando.setInt(8, objTipado.getStatus().valor);
        
        comando.setDate(9, java.sql.Date.valueOf(objTipado.getDataAtualizado()));
        
        if (objTipado.getDataEnviado() == null) {
            comando.setNull(10, Types.DATE);
        } else {
            comando.setDate(10, java.sql.Date.valueOf(objTipado.getDataEnviado()));
        }
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(11, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Funcionario obj = new Funcionario(resultado.getInt("id"));

        obj.setRegistro(resultado.getInt("registro"));

        obj.setNome(resultado.getString("nome"));
        obj.setEmail(resultado.getString("email"));
        obj.setTelefone(resultado.getString("telefone"));

        obj.setDataInicio(resultado.getDate("dataInicio").toLocalDate());

        if (resultado.getDate("dataSaida") != null) {
            obj.setDataSaida(resultado.getDate("dataSaida").toLocalDate());
        }

        obj.setDataAtualizado(resultado.getDate("dataAtualizado").toLocalDate());
        
        if (resultado.getDate("dataEnviado") != null) {
            obj.setDataEnviado(resultado.getDate("dataEnviado").toLocalDate());
        }
        
        if (resultado.getInt("id_escola") != 0) {
            EscolaDAO eDAO = new EscolaDAO();
            obj.setEscola(eDAO.consultar(resultado.getInt("id_escola")));
        }

        obj.setStatus(StatusFuncionario.fromInteger(resultado.getInt("status")));

        return obj;
    }

    protected final static String ATIVO = " status = " + StatusFuncionario.Ativo.valor;

    protected static String INSERT = "insert into funcionario (registro, nome, email, telefone, dataInicio, dataSaida, id_escola, status, dataAtualizado, dataEnviado) values(?,?,?,?,?,?,?,?,?,?)";
    protected static String UPDATE = "update funcionario set registro = ?, nome = ?, email = ?, telefone = ?, dataInicio = ?, dataSaida = ?, id_escola = ?, status = ?, dataAtualizado = ?, dataEnviado = ? where id = ?";
    protected static String DELETE = "delete from funcionario where id = ?";
    protected static String SELECT_ALL = "select * from funcionario where " + ATIVO;
    protected static String SELECT = "select * from funcionario where id = ? and " + ATIVO;
    protected static String SELECT_INATIVO = "select * from funcionario where id = ? and status = " + StatusFuncionario.Inativo.valor;

    public int cadastrar(Funcionario obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Funcionario obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluirLogico(int id) {
        Funcionario obj = (Funcionario) consultarGenerico(SELECT, id);
        obj.setStatus(StatusFuncionario.Inativo);
        obj.setDataSaida(LocalDate.now());
        return atualizar(obj);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Funcionario> listar() {
        return (List<Funcionario>) listarGenerico(SELECT_ALL);
    }

    public Funcionario consultar(int id) {
        return (Funcionario) consultarGenerico(SELECT, id);
    }

    public Funcionario consultarInativo(int id) {
        return (Funcionario) consultarGenerico(SELECT_INATIVO, id);
    }
    
    //incluir funcionários excluídos para que as UEs possam ser atualizadas
    protected static String SELECT_PARA_ATUALIZAR = "select * from funcionario where (dataEnviado is null) or (dataAtualizado >= dataEnviado)";

    public List<Funcionario> listarParaAtualizar() {
        return (List<Funcionario>) listarGenerico(SELECT_PARA_ATUALIZAR);
    }
    
    protected static String SELECT_REGISTRO = "select * from funcionario where registro = ? and " + ATIVO;

    public Funcionario consultarRegistro(int registro) {

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, registro);
                } catch (Exception e) {

                }
            }
        };
        return (Funcionario) consultarGenerico(SELECT_REGISTRO, param);

    }

}
