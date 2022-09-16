/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import shared.model.Usuario;
import shared.model.dao.UsuarioDAO;
import shared.util.Auditoria;
import shared.util.Criptografia;
import shared.util.FornecedorGson;
import shared.util.Validacao;

/**
 *
 * @author leona
 */
@WebServlet(name = "RealizarLogin", urlPatterns = {"/RealizarLogin"})
public class RealizarLogin extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        request.setCharacterEncoding("utf8");
        response.setContentType("text/html;charset=UTF-8");
        try {

            if (Validacao.validar(request,"dados") == null) {
                Auditoria.logInfo("Login falhou para login vazio");
                return;
            }

            //String login = request.getParameter("usuario");
            //String senha = request.getParameter("senha");
            Gson gson = FornecedorGson.getGson();

            JsonElement dadosEle = gson.fromJson(Validacao.validar(request,"dados"), JsonElement.class);
            JsonObject dadosJson = dadosEle.getAsJsonObject();

            String login = dadosJson.get("usuario").getAsString();
            String senha = dadosJson.get("senha").getAsString();

            Usuario uRecebido = new Usuario();
            uRecebido.setLogin(login);
            uRecebido.setSenha(senha);

            UsuarioDAO uDAO = new UsuarioDAO();
            
            //realizar salt na senha
            Usuario uAlvo = uDAO.consultarPorLogin(login);
            uRecebido.setSenha(Criptografia.hashSenha(uAlvo.getSalt(), senha));

            Usuario uAutenticado = uDAO.autenticar(uRecebido);

            if (uAutenticado == null) {
                //login falhou
                response.sendRedirect("login.html");
                Auditoria.logInfo("Login falhou para " + login);
                return;
            }

            HttpSession sessao = request.getSession();
            sessao.setAttribute("credencial", uAutenticado);
            sessao.setMaxInactiveInterval(60 * 60 * 10); //???

            Auditoria.logInfo("Login com sucesso para " + uAutenticado.getLogin());

            response.sendRedirect("index.html");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.getLogger(RealizarLogin.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Logger.getLogger(RealizarLogin.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
