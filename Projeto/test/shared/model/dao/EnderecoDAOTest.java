/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import shared.model.Endereco;
import shared.util.ConectaDB;
import shared.util.Configuracao;
import shared.util.GeradorObjetosTeste;

/**
 *
 * @author caueo
 */
public class EnderecoDAOTest {

    private Connection conexaoTeste;
    private EnderecoDAO DAOTeste;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //adicionar configuração para redirecionar para banco de dados 
        Configuracao.iniciarTeste();

        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new EnderecoDAO(conexaoTeste);
    }

    @After
    public void finalizar() throws SQLException {

        conexaoTeste.rollback();
        conexaoTeste.close();

        //desativar configuração 
        Configuracao.finalizarTeste();
    }

    @Test
    public void TesteCadastro() throws ClassNotFoundException, SQLException {

        Endereco obj = new Endereco();
        obj.setLogradouro("Teste Cadastro");
        obj.setCep("0");
        obj.setBairro(GeradorObjetosTeste.CadastrarBairroTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        Endereco objCadastrado = DAOTeste.consultar(id);

        assertEquals("Teste Cadastro", objCadastrado.getLogradouro());
        assertEquals(id, objCadastrado.getId());

    }

    @Test
    public void TesteAtualizar() throws ClassNotFoundException, SQLException {

        Endereco obj = new Endereco();
        obj.setLogradouro("Teste Atualizar");
        obj.setCep("0");
        obj.setBairro(GeradorObjetosTeste.CadastrarBairroTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        Endereco objCadastrado = DAOTeste.consultar(id);

        objCadastrado.setLogradouro("Teste Atualizado");
        DAOTeste.atualizar(objCadastrado);

        Endereco objAtualizado = DAOTeste.consultar(id);

        assertEquals("Teste Atualizado", objAtualizado.getLogradouro());
        assertEquals(id, objAtualizado.getId());

    }

    @Test
    public void TesteExcluir() throws ClassNotFoundException, SQLException {

        Endereco obj = new Endereco();
        obj.setLogradouro("Teste Exclusão");
        obj.setCep("0");
        obj.setBairro(GeradorObjetosTeste.CadastrarBairroTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        Endereco objCadastrado = DAOTeste.consultar(id);
        assertEquals("Teste Exclusão", objCadastrado.getLogradouro());
        assertEquals(id, objCadastrado.getId());

        DAOTeste.excluir(id);

        Endereco objExcluido = DAOTeste.consultar(id);
        assertEquals(null, objExcluido);

    }

    @Test
    public void TesteListar() throws ClassNotFoundException, SQLException {

        Endereco obj = new Endereco();
        obj.setLogradouro("Teste Listar");
        obj.setCep("0");
        obj.setBairro(GeradorObjetosTeste.CadastrarBairroTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        List<Endereco> lista = DAOTeste.listar();

        Endereco objListado = null;
        for (Endereco objLista : lista) {
            if (objLista.getId() == id) {
                objListado = objLista;
                break;
            }
        };

        assertEquals("Teste Listar", objListado.getLogradouro());
        assertEquals(id, objListado.getId());

    }
}
