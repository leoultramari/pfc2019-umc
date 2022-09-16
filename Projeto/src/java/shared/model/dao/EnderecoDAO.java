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
import shared.model.DadosAluno;
import shared.model.ObjetoDB;
import shared.model.Endereco;

/**
 *
 * @author leona
 */
public class EnderecoDAO extends DAOGenerico {

    public EnderecoDAO() {
        super();
    }

    public EnderecoDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Endereco objTipado = (Endereco) obj;

        comando.setString(1, objTipado.getCep());
        comando.setString(2, objTipado.getLogradouro());
        comando.setInt(3, objTipado.getNum());
        comando.setInt(4, objTipado.getBairro().getId());
        comando.setInt(5, objTipado.getDados().getId());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(6, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Endereco obj = new Endereco(resultado.getInt("id"));

        obj.setCep(resultado.getString("cep"));
        obj.setLogradouro(resultado.getString("logradouro"));
        obj.setNum(resultado.getInt("num"));

        BairroDAO bDAO = new BairroDAO();
        obj.setBairro(bDAO.consultar(resultado.getInt("id_bairro")));

        //set dados?
        DadosAluno dados = new DadosAluno();
        dados.setId(resultado.getInt("id_dados"));
        obj.setDados(dados);
        return obj;
    }

    protected static String INSERT = "insert into endereco (cep, logradouro, num, id_bairro, id_dados) values(?,?,?,?,?)";
    protected static String UPDATE = "update endereco set cep = ?, logradouro = ?, num = ?, id_bairro = ?, id_dados = ? where id = ?";
    protected static String DELETE = "delete from endereco where id = ?";
    protected static String SELECT_ALL = "select * from endereco";
    protected static String SELECT = "select * from endereco where id = ?";

    public int cadastrar(Endereco obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Endereco obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<Endereco> listar() {
        return (List<Endereco>) listarGenerico(SELECT_ALL);
    }

    public Endereco consultar(int id) {
        return (Endereco) consultarGenerico(SELECT, id);
    }

    protected static String SELECT_DADOS = "select * from endereco where id_dados = ?";

    public List<Endereco> listarPorDados(int id_dados) {

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_dados);
                } catch (Exception e) {

                }
            }
        };
        return (List<Endereco>) listarGenerico(SELECT_DADOS, param);

    }

}
