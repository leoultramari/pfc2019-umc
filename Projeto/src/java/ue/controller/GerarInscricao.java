/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import shared.model.Contato;
import shared.model.DadosAluno;
import shared.model.Documento;
import shared.model.Endereco;
import shared.model.Escola;
import shared.model.Inscricao;
import shared.model.StatusAluno;
import shared.model.StatusInscricao;
import shared.model.TipoContato;
import shared.model.TipoDocumento;
import shared.model.dao.BairroDAO;
import shared.model.dao.ContatoDAO;
import shared.model.dao.DadosAlunoDAO;
import shared.model.dao.DocumentoDAO;
import shared.model.dao.EnderecoDAO;
import shared.model.dao.InscricaoNovaDAO;
import shared.model.dao.TipoContatoDAO;
import shared.model.dao.TipoDocumentoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.FornecedorGson;
import shared.util.Validacao;

/**
 *
 * @author superalunocmc
 */
@WebServlet(name = "GerarInscricao", urlPatterns = {"/GerarInscricao"})
public class GerarInscricao extends HttpServlet {

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
            
            Gson gson = FornecedorGson.getGson();

            JsonElement dadosEle = gson.fromJson(Validacao.validar(request,"dados"), JsonElement.class);
            JsonObject dadosJson = dadosEle.getAsJsonObject();

            JsonElement enderecoEle = gson.fromJson(Validacao.validar(request,"endereco"), JsonElement.class);
            JsonObject endJson = enderecoEle.getAsJsonObject();

            JsonElement contatosEle = gson.fromJson(Validacao.validar(request,"contatos"), JsonElement.class);
            JsonObject conJson = contatosEle.getAsJsonObject();

            JsonElement documentosEle = gson.fromJson(Validacao.validar(request,"documentos"), JsonElement.class);
            JsonObject docJson = documentosEle.getAsJsonObject();

            //System.out.println(dadosJson.toString());
            //System.out.println(endJson.toString());
            //System.out.println(conJson.toString());
            //System.out.println(docJson.toString());

            Inscricao i = new Inscricao();

            DadosAluno dados = gson.fromJson(dadosEle, DadosAluno.class);
            DadosAlunoDAO dadosDAO = new DadosAlunoDAO();
            dados.setId(dadosDAO.cadastrar(dados));
            
            Endereco e = gson.fromJson(enderecoEle, Endereco.class);

            BairroDAO bDAO = new BairroDAO();
            e.setBairro(bDAO.consultar(endJson.get("id_bairro").getAsInt()));
            //System.out.println(e.getBairro().getNome());

            e.setDados(dados);
            //dados.setEndereco(e);
            EnderecoDAO eDAO = new EnderecoDAO();
            e.setId(eDAO.cadastrar(e));

            TipoDocumentoDAO tdDAO = new TipoDocumentoDAO();
            DocumentoDAO dDAO = new DocumentoDAO();
            
            int iDoc = 1;
            while (docJson.get("tipoDocumento" + iDoc) != null) {

                Documento d = new Documento();

                
                TipoDocumento t = tdDAO.consultar(docJson.get("tipoDocumento" + iDoc).getAsInt());
                d.setTipoDocumento(t);

                d.setDado(docJson.get("documento" + iDoc).getAsString());
                if (docJson.get("validadeDocumento" + iDoc) != null) {
                    d.setValidade(gson.fromJson(docJson.get("validadeDocumento" + iDoc), LocalDate.class));
                }

                d.setDados(dados);
                //dados.getDocumentos().add(d);
                
                d.setId(dDAO.cadastrar(d));

                iDoc++;
            }
            
            TipoContatoDAO tcDAO = new TipoContatoDAO();
            ContatoDAO cDAO = new ContatoDAO();

            int iCon = 1;
            while (conJson.get("tipoContato" + iCon) != null) {

                Contato c = new Contato();

                
                TipoContato t = tcDAO.consultar(conJson.get("tipoContato" + iCon).getAsInt());
                c.setTipoContato(t);

                c.setDado(conJson.get("contato" + iCon).getAsString());

                c.setDados(dados);
                //dados.getContatos().add(c);
                
                c.setId(cDAO.cadastrar(c));

                iCon++;
            }

            i.setDados(dados);
            //Buscar id da escola atual
            //i.setEscolaOriginal(escolaOriginal);
            Escola test = new Escola();
            test.setId(Configuracao.getInt("autenticacao", "id"));
            i.setEscolaOriginal(test);
            
            i.setStatus(StatusInscricao.Aberta);
            i.setStatusMatricula(StatusAluno.Pendente);
            
            InscricaoNovaDAO iDAO = new InscricaoNovaDAO();
            int iId = iDAO.cadastrar(i);

            Auditoria.logInfo("Cadastrou inscrição " + iId);
            
            out.print(Configuracao.getContextoAtual());
            
            /*
            System.out.println("DadosAluno " + d.getNome());
            System.out.println(d.getDataNascimento());

            TipoDocumentoDAO tDAO = new TipoDocumentoDAO();
            TipoDocumento tipo = tDAO.consultar(documentos.get("tipoDocumento").getAsInt());

            System.out.println(documentos.get("tipoDocumento"));
            System.out.println(documentos.get("tipoDocumento").getAsInt());
            System.out.println(tipo.getNome());

            System.out.println(dados.get("nome").getAsString());
             */
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
