/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shared.model.Funcionario;
import shared.model.StatusFuncionario;
import shared.model.dao.EscolaDAO;
import shared.model.dao.FuncionarioDAO;
import shared.util.Auditoria;
import shared.util.FornecedorGson;
import shared.util.Validacao;

/**
 *
 * @author leona
 */
@WebServlet(name = "CadastrarFuncionario", urlPatterns = {"/CadastrarFuncionario"})
public class CadastrarFuncionario extends HttpServlet {

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
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {

            Gson gson = FornecedorGson.getGson();

            JsonElement dadosEle = gson.fromJson(Validacao.validar(request,"dados"), JsonElement.class);
            JsonObject dadosJson = dadosEle.getAsJsonObject();

            Funcionario obj = gson.fromJson(dadosEle, Funcionario.class);
            
            EscolaDAO eDAO = new EscolaDAO();
            obj.setEscola(eDAO.consultar(dadosJson.get("id_escola").getAsInt()));
            
            obj.setStatus(StatusFuncionario.Ativo);
            
            obj.setDataAtualizado(LocalDate.now());
            
            FuncionarioDAO DAO = new FuncionarioDAO();      
            int iId = DAO.cadastrar(obj);

            Auditoria.logInfo("Cadastrou funcionário " + iId);
            
           
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
        processRequest(request, response);
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
        processRequest(request, response);
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
