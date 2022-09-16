/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import shared.model.Cargo;
import shared.model.Usuario;
import shared.model.StatusUsuario;
import shared.model.dao.CargoDAO;
import shared.model.dao.UsuarioDAO;
import shared.util.ConectaDB;

/**
 *
 * @author leona
 */
public class RemoverUsuarioTest {

    private Connection conexaoTeste;
    private UsuarioDAO DAOTeste;

    private RemoverUsuario servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @Before
    public void preparar() throws ClassNotFoundException, SQLException {

        //redirecionar para banco de dados de teste
        conexaoTeste = ConectaDB.getConexao();
        conexaoTeste.setAutoCommit(false);

        DAOTeste = new UsuarioDAO();

        servlet = new RemoverUsuario();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

    }

    @After
    public void finalizar() throws SQLException {

        //desativar banco de dados de teste
        conexaoTeste.rollback();
        conexaoTeste.close();
    }
    
    public Cargo CadastrarCargoTeste(){
        CargoDAO cDAO = new CargoDAO();
        
        Cargo c = new Cargo();
        c.setNome("Teste");
        
        int id = cDAO.cadastrar(c);      
        return cDAO.consultar(id);
    }

    @Test
    public void TestarRemoverUsuario() throws ServletException, IOException {

        Usuario obj = new Usuario();
        obj.setStatus(StatusUsuario.Ativo);
        obj.setCargo(CadastrarCargoTeste());
        obj.setLogin("Teste");
        obj.setSenha("Teste");

        int id = DAOTeste.cadastrar(obj);
        
        System.out.println(id);

        when(request.getParameter("id")).thenReturn("" + id);
        servlet.doPost(request, response);

        Usuario objCadastrado = DAOTeste.consultarInativo(id);

        assertNotNull(objCadastrado);
        assertEquals(id, objCadastrado.getId());
        assertEquals(StatusUsuario.Inativo, objCadastrado.getStatus());

    }

}
