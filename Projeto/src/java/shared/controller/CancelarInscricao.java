/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.Validacao;

/**
 *
 * @author superalunocmc
 */
@WebServlet(name = "CancelarInscricao", urlPatterns = {"/CancelarInscricao"})
public class CancelarInscricao extends HttpServlet {

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

            InscricaoDAO iDAO = new InscricaoDAO();
            Inscricao i = iDAO.consultar(id);

            //Verificar se podemos cancelar essa inscrição
            switch (Configuracao.getContextoAtual()) {
                case UE:
                    if (i.getStatus() != StatusInscricao.Aberta && i.getStatus() != StatusInscricao.RecebidaUE) {
                        Auditoria.logAviso("Tentou cancelar inscrição " + id + " com status " + i.getStatus());
                        return;
                    }
                    if (i.getStatus() == StatusInscricao.RecebidaUE) {
                        if (i.getEscolaAlocada().getId() != Configuracao.getInt("autenticacao", "id")) {
                            Auditoria.logAviso("Tentou cancelar inscrição " + id + " de outra UE " + i.getEscolaAlocada().getNome());
                            return;
                        }
                    }
                    break;
                case SME:
                    if (i.getStatus() != StatusInscricao.RecebidaSME && i.getStatus() != StatusInscricao.Alocada) {
                        Auditoria.logAviso("Tentou cancelar inscrição " + id + " com status " + i.getStatus());
                        return;
                    }
                    break;
                default:
                    return;
            }

            iDAO.excluirLogico(id);
            Auditoria.logInfo("Cancelou inscrição " + id);

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
