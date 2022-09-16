/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;

/**
 *
 * @author leona
 */
public class RealizarLoginTestMockito {

    private RealizarLogin servlet;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;

    @Before
    public void inicializar() {
        servlet = new RealizarLogin();
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void deveValidarLoginSenhaValidos() throws ServletException, IOException {

        when(request.getParameter("usuario")).thenReturn("sme");
        when(request.getParameter("senha")).thenReturn("sme");

        servlet.doPost(request, response);

        verify(response).sendRedirect("index.html");

    }

    @Test
    public void deveValidarLoginSenhaInvalidos() throws ServletException, IOException {

        when(request.getParameter("usuario")).thenReturn("aaa");
        when(request.getParameter("senha")).thenReturn("aaa");

        servlet.doPost(request, response);

        verify(response).sendRedirect("login.html");

    }

}
