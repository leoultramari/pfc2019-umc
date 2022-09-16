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
import shared.model.Cargo;
import shared.util.ConectaDB;
import shared.util.Configuracao;

/**
 *
 * @author caueo
 */
public class CargoDAOTest {

    private Connection conexaoTeste;
    private CargoDAO DAOTeste;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //adicionar configuração para redirecionar para banco de dados 
        Configuracao.iniciarTeste();

        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new CargoDAO(conexaoTeste);
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

        Cargo c = new Cargo();
        c.setNome("Teste Cadastro");

        int id = DAOTeste.cadastrar(c);

        Cargo cCadastrado = DAOTeste.consultar(id);

        assertEquals("Teste Cadastro", cCadastrado.getNome());
        assertEquals(id, cCadastrado.getId());

    }

    @Test
    public void TesteAtualizar() throws ClassNotFoundException, SQLException {

        Cargo c = new Cargo();
        c.setNome("Teste Atualizar");

        int id = DAOTeste.cadastrar(c);

        Cargo cCadastrado = DAOTeste.consultar(id);

        cCadastrado.setNome("Teste Atualizado");
        DAOTeste.atualizar(cCadastrado);

        Cargo cAtualizado = DAOTeste.consultar(id);

        assertEquals("Teste Atualizado", cAtualizado.getNome());
        assertEquals(id, cAtualizado.getId());

    }

    @Test
    public void TesteExcluir() throws ClassNotFoundException, SQLException {

        Cargo c = new Cargo();
        c.setNome("Teste Exclusão");

        int id = DAOTeste.cadastrar(c);

        Cargo cCadastrado = DAOTeste.consultar(id);
        assertEquals("Teste Exclusão", cCadastrado.getNome());
        assertEquals(id, cCadastrado.getId());

        DAOTeste.excluir(id);

        Cargo eExcluido = DAOTeste.consultar(id);
        assertEquals(null, eExcluido);

    }

    @Test
    public void TesteListar() throws ClassNotFoundException, SQLException {

        Cargo c = new Cargo();
        c.setNome("Teste Listar");

        int id = DAOTeste.cadastrar(c);

        List<Cargo> lista = DAOTeste.listar();

        Cargo cListada = null;
        for (Cargo cLista : lista) {
            if (cLista.getId() == id) {
                cListada = cLista;
                break;
            }
        };

        assertEquals("Teste Listar", cListada.getNome());
        assertEquals(id, cListada.getId());

    }
}
