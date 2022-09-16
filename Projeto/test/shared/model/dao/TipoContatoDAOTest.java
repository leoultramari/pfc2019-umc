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
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import shared.model.TipoContato;
import shared.util.ConectaDB;
import shared.util.Configuracao;

/**
 *
 * @author leona
 */
public class TipoContatoDAOTest {

    private Connection conexaoTeste;
    private TipoContatoDAO DAOTeste;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //adicionar configuração para redirecionar para banco de dados 
        Configuracao.iniciarTeste();

        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new TipoContatoDAO(conexaoTeste);
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

        TipoContato obj = new TipoContato();
        obj.setNome("Teste Cadastro");

        int id = DAOTeste.cadastrar(obj);

        TipoContato objCadastrado = DAOTeste.consultar(id);

        assertEquals("Teste Cadastro", objCadastrado.getNome());
        assertEquals(id, objCadastrado.getId());

    }

    @Test
    public void TesteAtualizar() throws ClassNotFoundException, SQLException {

        TipoContato obj = new TipoContato();
        obj.setNome("Teste Atualizar");

        int id = DAOTeste.cadastrar(obj);

        TipoContato objCadastrado = DAOTeste.consultar(id);

        objCadastrado.setNome("Teste Atualizado");
        DAOTeste.atualizar(objCadastrado);

        TipoContato objAtualizado = DAOTeste.consultar(id);

        assertEquals("Teste Atualizado", objAtualizado.getNome());
        assertEquals(id, objAtualizado.getId());

    }

    @Test
    public void TesteExcluir() throws ClassNotFoundException, SQLException {

        TipoContato obj = new TipoContato();
        obj.setNome("Teste Exclusão");

        int id = DAOTeste.cadastrar(obj);

        TipoContato objCadastrado = DAOTeste.consultar(id);
        assertEquals("Teste Exclusão", objCadastrado.getNome());
        assertEquals(id, objCadastrado.getId());

        DAOTeste.excluir(id);

        TipoContato objExcluido = DAOTeste.consultar(id);
        assertEquals(null, objExcluido);

    }

    @Test
    public void TesteListar() throws ClassNotFoundException, SQLException {

        TipoContato obj = new TipoContato();
        obj.setNome("Teste Listar");

        int id = DAOTeste.cadastrar(obj);

        List<TipoContato> lista = DAOTeste.listar();

        TipoContato objListado = null;
        for (TipoContato objLista : lista) {
            if (objLista.getId() == id) {
                objListado = objLista;
                break;
            }
        };

        assertEquals("Teste Listar", objListado.getNome());
        assertEquals(id, objListado.getId());

    }

}
