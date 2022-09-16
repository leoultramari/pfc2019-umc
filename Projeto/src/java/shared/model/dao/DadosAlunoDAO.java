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
import java.util.ArrayList;
import java.util.List;
import shared.model.Contato;
import shared.model.ObjetoDB;
import shared.model.DadosAluno;
import shared.model.Documento;
import shared.model.Endereco;

/**
 *
 * @author leona
 */
public class DadosAlunoDAO extends DAOGenerico {

    public DadosAlunoDAO() {
        super();
    }

    public DadosAlunoDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        DadosAluno objTipado = (DadosAluno) obj;

        comando.setString(1, objTipado.getNome());
        comando.setString(2, String.valueOf(objTipado.getSexo()));
        comando.setDate(3, java.sql.Date.valueOf(objTipado.getDataNascimento()));
        comando.setString(4, objTipado.getFiliacao1());
        comando.setString(5, objTipado.getFiliacao2());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(6, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        DadosAluno obj = new DadosAluno(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));
        obj.setSexo(resultado.getString("sexo").charAt(0));
        obj.setDataNascimento(resultado.getDate("dataNascimento").toLocalDate());
        obj.setFiliacao1(resultado.getString("filiacao1"));
        obj.setFiliacao2(resultado.getString("filiacao2"));

        EnderecoDAO eDAO = new EnderecoDAO();
        List<Endereco> lista = eDAO.listarPorDados(resultado.getInt("id"));
        if (!lista.isEmpty()) {
            obj.setEndereco(lista.get(0));
        }

        DocumentoDAO dDAO = new DocumentoDAO();
        obj.setDocumentos((ArrayList<Documento>) dDAO.listarPorDados(resultado.getInt("id")));

        ContatoDAO cDAO = new ContatoDAO();
        obj.setContatos((ArrayList<Contato>) cDAO.listarPorDados(resultado.getInt("id")));

        return obj;
    }
    
    //Não preenche Contatos e Documentos e outros campos
    protected ObjetoDB gerarObjetoSimples(ResultSet resultado) throws SQLException, ClassNotFoundException {
        DadosAluno obj = new DadosAluno(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));

        EnderecoDAO eDAO = new EnderecoDAO();
        List<Endereco> lista = eDAO.listarPorDados(resultado.getInt("id"));
        if (!lista.isEmpty()) {
            obj.setEndereco(lista.get(0));
        }

        return obj;
    }

    protected static String INSERT = "insert into dadosAluno (nome, sexo, dataNascimento, filiacao1, filiacao2) values(?,?,?,?,?)";
    protected static String UPDATE = "update dadosAluno set nome = ?, sexo = ?, dataNascimento = ?, filiacao1 = ?, filiacao2 = ? where id = ?";
    protected static String DELETE = "delete from dadosAluno where id = ?";
    protected static String SELECT_ALL = "select * from dadosAluno";
    protected static String SELECT = "select * from dadosAluno where id = ?";

    public int cadastrar(DadosAluno obj) {
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(DadosAluno obj) {
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id) {
        return excluirGenerico(id, DELETE);
    }

    public List<DadosAluno> listar() {
        return (List<DadosAluno>) listarGenerico(SELECT_ALL);
    }

    public DadosAluno consultar(int id) {
        return (DadosAluno) consultarGenerico(SELECT, id);
    }
    
    public DadosAluno consultarSimples(int id) {
        return (DadosAluno) consultarGenerico(SELECT, id, true);
    }

}
