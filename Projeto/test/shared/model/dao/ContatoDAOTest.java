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
import shared.model.Contato;
import shared.util.ConectaDB;
import shared.util.Configuracao;
import shared.util.GeradorObjetosTeste;

/**
 *
 * @author caueo
 */
public class ContatoDAOTest {

    private Connection conexaoTeste;
    private ContatoDAO DAOTeste;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //adicionar configuração para redirecionar para banco de dados 
        Configuracao.iniciarTeste();

        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new ContatoDAO(conexaoTeste);
    }

    @After
    public void finalizar() throws SQLException {

        //desativar configuração 
        conexaoTeste.rollback();
        conexaoTeste.close();

        //desativar configuração 
        Configuracao.finalizarTeste();
    }

    @Test
    public void TesteCadastro() throws ClassNotFoundException, SQLException {

        Contato obj = new Contato();
        obj.setDado("Teste Cadastro");
        obj.setTipoContato(GeradorObjetosTeste.CadastrarTipoContatoTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        Contato objCadastrado = DAOTeste.consultar(id);

        assertEquals("Teste Cadastro", objCadastrado.getDado());
        assertEquals(id, objCadastrado.getId());

    }

    @Test
    public void TesteAtualizar() throws ClassNotFoundException, SQLException {

        Contato obj = new Contato();
        obj.setDado("Teste Atualizar");
        obj.setTipoContato(GeradorObjetosTeste.CadastrarTipoContatoTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        Contato objCadastrado = DAOTeste.consultar(id);

        objCadastrado.setDado("Teste Atualizado");
        DAOTeste.atualizar(objCadastrado);

        Contato objAtualizado = DAOTeste.consultar(id);

        assertEquals("Teste Atualizado", objAtualizado.getDado());
        assertEquals(id, objAtualizado.getId());

    }

    @Test
    public void TesteExcluir() throws ClassNotFoundException, SQLException {

        Contato obj = new Contato();
        obj.setDado("Teste Exclusão");
        obj.setTipoContato(GeradorObjetosTeste.CadastrarTipoContatoTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        Contato objCadastrado = DAOTeste.consultar(id);
        assertEquals("Teste Exclusão", objCadastrado.getDado());
        assertEquals(id, objCadastrado.getId());

        DAOTeste.excluir(id);

        Contato objExcluido = DAOTeste.consultar(id);
        assertEquals(null, objExcluido);

    }

    @Test
    public void TesteListar() throws ClassNotFoundException, SQLException {

        Contato obj = new Contato();
        obj.setDado("Teste Listar");
        obj.setTipoContato(GeradorObjetosTeste.CadastrarTipoContatoTeste(conexaoTeste));
        obj.setDados(GeradorObjetosTeste.CadastrarDadosAlunoVazioTeste(conexaoTeste));

        int id = DAOTeste.cadastrar(obj);

        List<Contato> lista = DAOTeste.listar();

        Contato objListado = null;
        for (Contato objLista : lista) {
            if (objLista.getId() == id) {
                objListado = objLista;
                break;
            }
        };

        assertEquals("Teste Listar", objListado.getDado());
        assertEquals(id, objListado.getId());

    }
}
