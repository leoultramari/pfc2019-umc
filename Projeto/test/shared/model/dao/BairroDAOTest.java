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
import shared.model.Bairro;
import shared.util.ConectaDB;
import shared.util.Configuracao;
import shared.util.GeradorObjetosTeste;

/**
 *
 * @author caueo
 */
public class BairroDAOTest {

    private Connection conexaoTeste;
    private BairroDAO DAOTeste;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //System.out.println("Iniciando teste");
        //adicionar configuração para redirecionar para banco de dados 
        Configuracao.iniciarTeste();

        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new BairroDAO(conexaoTeste);
        //System.out.println("Teste iniciado");
    }

    @After
    public void finalizar() throws SQLException {

        //System.out.println("Finalizando teste");
        conexaoTeste.rollback();
        conexaoTeste.close();

        //desativar configuração 
        Configuracao.finalizarTeste();
        //System.out.println("Teste finalizado");
    }
     
    @Test 
    public void TesteCadastro() throws ClassNotFoundException, SQLException{ 
         
        Bairro b = new Bairro(); 
        b.setNome("Teste Cadastro"); 
        b.setEscola(GeradorObjetosTeste.CadastrarEscolaTeste(conexaoTeste));
         
        int id = DAOTeste.cadastrar(b); 
         
        Bairro bCadastrado = DAOTeste.consultar(id); 
         
        assertEquals("Teste Cadastro", bCadastrado.getNome()); 
        assertEquals(id, bCadastrado.getId());         
    } 
     
    @Test 
    public void TesteAtualizar() throws ClassNotFoundException, SQLException { 
         
        Bairro b = new Bairro(); 
        b.setNome("Teste Atualizar"); 
        b.setEscola(GeradorObjetosTeste.CadastrarEscolaTeste(conexaoTeste));
         
        int id = DAOTeste.cadastrar(b); 
         
        Bairro bCadastrado = DAOTeste.consultar(id); 
         
        bCadastrado.setNome("Teste Atualizado"); 
        DAOTeste.atualizar(bCadastrado);       
         
        Bairro bAtualizado = DAOTeste.consultar(id); 
         
        assertEquals("Teste Atualizado", bAtualizado.getNome()); 
        assertEquals(id, bAtualizado.getId());         
    } 
     
    @Test 
    public void TesteExcluir() throws ClassNotFoundException, SQLException{ 
         
        Bairro b = new Bairro(); 
        b.setNome("Teste Exclusão"); 
        b.setEscola(GeradorObjetosTeste.CadastrarEscolaTeste(conexaoTeste));
         
        int id = DAOTeste.cadastrar(b); 
         
        Bairro bCadastrado = DAOTeste.consultar(id); 
        assertEquals("Teste Exclusão", bCadastrado.getNome()); 
        assertEquals(id, bCadastrado.getId()); 
         
        DAOTeste.excluir(id); 
         
        Bairro bExcluido = DAOTeste.consultar(id); 
        assertEquals(null, bExcluido);       
    } 
     
    @Test 
    public void TesteListar() throws ClassNotFoundException, SQLException{ 
         
        Bairro b = new Bairro(); 
        b.setNome("Teste Listar"); 
        b.setEscola(GeradorObjetosTeste.CadastrarEscolaTeste(conexaoTeste));
         
        int id = DAOTeste.cadastrar(b); 
         
        List<Bairro> lista = DAOTeste.listar(); 
         
        Bairro bListada = null; 
        for(Bairro bLista : lista){ 
            if(bLista.getId() == id){ 
                bListada = bLista; 
                break; 
            } 
        }; 
         
        assertEquals("Teste Listar", bListada.getNome()); 
        assertEquals(id, bListada.getId());         
    }
    
}
