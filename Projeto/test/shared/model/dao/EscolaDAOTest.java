/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import shared.model.Escola;
import shared.util.ConectaDB;
import shared.util.Configuracao;

/**
 *
 * @author alunocmc
 */
public class EscolaDAOTest {

    private Connection conexaoTeste;
    private EscolaDAO DAOTeste;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //adicionar configuração para redirecionar para banco de dados 
        Configuracao.iniciarTeste();

        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new EscolaDAO(conexaoTeste);
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

        Escola e = new Escola();
        e.setNome("Teste Cadastro");

        int id = DAOTeste.cadastrar(e);

        Escola eCadastrado = DAOTeste.consultar(id);

        assertEquals("Teste Cadastro", eCadastrado.getNome());
        assertEquals(id, eCadastrado.getId());

    }

    @Test
    public void TesteAtualizar() throws ClassNotFoundException, SQLException {

        Escola e = new Escola();
        e.setNome("Teste Atualizar");

        int id = DAOTeste.cadastrar(e);

        Escola eCadastrado = DAOTeste.consultar(id);

        eCadastrado.setNome("Teste Atualizado");
        DAOTeste.atualizar(eCadastrado);

        Escola eAtualizado = DAOTeste.consultar(id);

        assertEquals("Teste Atualizado", eAtualizado.getNome());
        assertEquals(id, eAtualizado.getId());

    }

    @Test
    public void TesteExcluir() throws ClassNotFoundException, SQLException {

        Escola e = new Escola();
        e.setNome("Teste Exclusão");

        int id = DAOTeste.cadastrar(e);

        Escola eCadastrado = DAOTeste.consultar(id);
        assertEquals("Teste Exclusão", eCadastrado.getNome());
        assertEquals(id, eCadastrado.getId());

        DAOTeste.excluir(id);

        Escola eExcluido = DAOTeste.consultar(id);
        assertEquals(null, eExcluido);

    }

    @Test
    public void TesteListar() throws ClassNotFoundException, SQLException {

        Escola e = new Escola();
        e.setNome("Teste Listar");

        int id = DAOTeste.cadastrar(e);

        List<Escola> lista = DAOTeste.listar();

        Escola eListada = null;
        for (Escola eLista : lista) {
            if (eLista.getId() == id) {
                eListada = eLista;
                break;
            }
        };

        assertEquals("Teste Listar", eListada.getNome());
        assertEquals(id, eListada.getId());

    }

}
