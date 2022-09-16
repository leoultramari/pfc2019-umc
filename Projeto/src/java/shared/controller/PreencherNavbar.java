/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shared.model.Cargo;
import shared.model.Permissao;
import shared.model.Usuario;
import shared.model.dao.PermissaoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;
import ue.bll.ConsumidorErroUE;

/**
 *
 * @author leona
 */
@WebServlet(name = "PreencherNavbar", urlPatterns = {"/PreencherNavbar"})
public class PreencherNavbar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private JsonObject gerarMenu(String nome) {
        JsonObject menu = new JsonObject();
        menu.addProperty("nome", nome);
        JsonArray menuItens = new JsonArray();
        menu.add("itens", menuItens);
        return menu;
    }

    private JsonObject gerarItem(String nome, String URL) {
        JsonObject item = new JsonObject();
        item.addProperty("nome", nome);
        item.addProperty("URL", URL);
        return item;
    }

    private JsonElement gerarNavbar(Usuario u) {

        Cargo c = u.getCargo();
        JsonArray itens = new JsonArray();

        PermissaoDAO pDAO = new PermissaoDAO();
        List<Permissao> permissoes = pDAO.listarPorCargo(c.getId());

        //if (c.isPermVisualizarUsuarios()) {
        if (pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarUsuarios") != null) {

            JsonObject menuAdmin = gerarMenu("Administrador - Usuários");
            menuAdmin.get("itens").getAsJsonArray().add(gerarItem("Lista Usuários", "/admin/usuario/lista-usuarios.html"));
            if (pDAO.consultarParaCargoPorNome(c.getId(), "ManterUsuarios") != null) {
                menuAdmin.get("itens").getAsJsonArray().add(gerarItem("Cadastrar Usuário", "/admin/usuario/cadastrar-usuario.html"));
            }
            //menuAdmin.get("itens").getAsJsonArray().add(gerarItem("Configuração", "/admin/config.html"));
            itens.add(menuAdmin);
        }

        switch (Configuracao.getContextoAtual()) {
            case SME:

                //negar funções SME para funcionários UE
                if (u.getEscola() != null) {
                    break;
                }

                JsonObject menuSME;

                if (pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarEscolas") != null) {
                    menuSME = gerarMenu("SME - Controle");
                    menuSME.get("itens").getAsJsonArray().add(gerarItem("Lista Escolas", "/SME/escola/controle-escolas.html"));
                    itens.add(menuSME);
                }

                if (pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarInscricoes") != null) {
                    menuSME = gerarMenu("SME - Inscrições");
                    menuSME.get("itens").getAsJsonArray().add(gerarItem("Inscrições Recebidas", "/SME/inscricao/lista-inscricoes.html"));
                    menuSME.get("itens").getAsJsonArray().add(gerarItem("Inscrições Arquivadas", "/SME/inscricao/lista-inscricoes-arquivadas.html"));
                    itens.add(menuSME);
                }
                /*
                if (pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarAlunos") != null) {
                    menuSME = gerarMenu("SME - Alunos");
                    menuSME.get("itens").getAsJsonArray().add(gerarItem("Lista Alunos por Turma", "/SME/aluno/lista-alunos.html"));
                    itens.add(menuSME);
                }
                */
                if (pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarFuncionarios") != null) {
                    menuSME = gerarMenu("SME - Funcionários");
                    menuSME.get("itens").getAsJsonArray().add(gerarItem("Lista Funcionários", "/SME/funcionario/lista-funcionarios.html"));
                    if (pDAO.consultarParaCargoPorNome(c.getId(), "ManterFuncionarios") != null) {
                        menuSME.get("itens").getAsJsonArray().add(gerarItem("Cadastrar Funcionário", "/SME/funcionario/cadastrar-funcionario.html"));
                    }
                    itens.add(menuSME);
                }

                break;
            case UE:

                //negar funções UE para funcionários SME e admins
                if (u.getEscola() == null) {
                    break;
                }

                if ((pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarInscricoes") != null)) {
                    JsonObject menuInscricoes = gerarMenu("UE - Inscrições");
                    if ((pDAO.consultarParaCargoPorNome(c.getId(), "ManterInscricoes") != null)) {
                        menuInscricoes.get("itens").getAsJsonArray().add(gerarItem("Realizar Inscrição", "/inscricao/form-inscricao.html"));
                    }
                    menuInscricoes.get("itens").getAsJsonArray().add(gerarItem("Inscrições Cadastradas", "/UE/inscricao/lista-inscricoes.html"));
                    menuInscricoes.get("itens").getAsJsonArray().add(gerarItem("Inscrições Recebidas", "/UE/inscricao/lista-inscricoes-recebidas.html"));
                    itens.add(menuInscricoes);
                }

                if ((pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarAlunos") != null)) {
                    JsonObject menuMatriculas = gerarMenu("UE - Alunos");
                    if ((pDAO.consultarParaCargoPorNome(c.getId(), "ManterAlunos") != null)) {
                        //menuMatriculas.get("itens").getAsJsonArray().add(gerarItem("Realizar Matrícula", "/UE/matricula/cadastro.html"));
                    }
                    menuMatriculas.get("itens").getAsJsonArray().add(gerarItem("Lista Matrículas", "/UE/aluno/lista-matriculas.html"));
                    menuMatriculas.get("itens").getAsJsonArray().add(gerarItem("Lista Alunos", "/UE/aluno/lista-alunos-turma.html"));
                    menuMatriculas.get("itens").getAsJsonArray().add(gerarItem("Lista Turmas", "/UE/aluno/lista-turmas.html"));
                    itens.add(menuMatriculas);
                }
                

                if ((pDAO.consultarParaCargoPorNome(c.getId(), "VisualizarFuncionarios") != null)) {
                    JsonObject menuUE = gerarMenu("UE - Funcionários");
                    menuUE.get("itens").getAsJsonArray().add(gerarItem("Lista Funcionários", "/UE/funcionario/lista-funcionarios.html"));
                    itens.add(menuUE);
                }

                break;
        }
        return itens;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            Gson gson = new Gson();
            JsonObject dados = new JsonObject();

            Usuario usuarioAtual = (Usuario) request.getSession().getAttribute("credencial");
            if (usuarioAtual != null) {

                dados.add("menus", gerarNavbar(usuarioAtual));

                dados.addProperty("login", usuarioAtual.getLogin());

                dados.addProperty("contexto", Configuracao.getContextoAtual().toString());

                if (usuarioAtual.getCargo() != null) {
                    dados.addProperty("cargo", usuarioAtual.getCargo().getNome());
                }

                if (usuarioAtual.getEscola() != null) {
                    dados.addProperty("escola", usuarioAtual.getEscola().getNome());
                }

                if (ConsumidorErroUE.isPossuiErro()) {
                    dados.addProperty("erro", ConsumidorErroUE.getErro());
                }

                if (Configuracao.get("autenticacao", "id") != null) {
                    dados.addProperty("id", Configuracao.getInt("autenticacao", "id"));
                }

                //adicionar dados relevantes à navbar
            }
            //Auditoria.logDepurar(gson.toJson(dados));
            out.print(gson.toJson(dados));

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
