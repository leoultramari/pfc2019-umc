/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shared.model.Aluno;
import shared.model.StatusAluno;
import shared.model.TipoMensagem;
import shared.model.dao.AlunoDAO;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;
import shared.util.Validacao;

/**
 *
 * @author leona
 */
@WebServlet(name = "RemoverAluno", urlPatterns = {"/RemoverAluno"})
public class RemoverAluno extends HttpServlet {

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
        try (PrintWriter out = response.getWriter()) {

            if (Validacao.validar(request,"id") == null) {
                return;
            }

            int id = Validacao.validarInt(request,"id");

            AlunoDAO aDAO = new AlunoDAO();
            Aluno a = aDAO.consultar(id);
            a.setStatus(StatusAluno.Cancelado);
            aDAO.atualizar(a);
            Auditoria.logInfo("Removeu aluno " + id);

            Gson gson = FornecedorGson.getGson();
            JsonObject dados = new JsonObject();
            dados.addProperty("idEscolaOriginal", a.getIdEscolaOriginal());
            dados.addProperty("idInscricao", a.getIdInscricao());
            dados.addProperty("status", StatusAluno.Cancelado.valor);
            
            Session session = GerenciadorActiveMQ.getSession();
            TextMessage resposta = session.createTextMessage(gson.toJson(dados));
            resposta.setIntProperty("tipo", TipoMensagem.Matriculas.valor);
            resposta.setJMSPriority(7);
            AutenticacaoMensagem.assinar(resposta);
            IntegridadeMensagem.gravarMD5(resposta);
            ProdutorJMS.produzir("sme", resposta);
            Auditoria.logInfo("Cancelamento da matrícula " + id + ", inscrição " + a.imprimirId() + " enviado para a SME");

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
