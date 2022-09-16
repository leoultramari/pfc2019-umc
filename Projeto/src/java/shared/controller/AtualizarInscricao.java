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
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.TipoContato;
import shared.model.TipoDocumento;
import shared.model.dao.BairroDAO;
import shared.model.dao.ContatoDAO;
import shared.model.dao.DadosAlunoDAO;
import shared.model.dao.DocumentoDAO;
import shared.model.dao.EnderecoDAO;
import shared.model.dao.InscricaoDAO;
import shared.model.dao.TipoContatoDAO;
import shared.model.dao.TipoDocumentoDAO;
import shared.util.Auditoria;
import shared.util.Configuracao;

import shared.util.FornecedorGson;
import shared.util.Validacao;

/**
 *
 * @author leona
 */
@WebServlet(name = "AtualizarInscricao", urlPatterns = {"/AtualizarInscricao"})
public class AtualizarInscricao extends HttpServlet {

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

            if(Validacao.validar(request,"id") == null) {
                return;
            } 
            
            int id = Validacao.validarInt(request,"id");
            
            InscricaoDAO iDAO = new InscricaoDAO();
            Inscricao i = iDAO.consultar(id);
      
            //Verificar se podemos editar essa inscrição
            switch (Configuracao.getContextoAtual()){
                    case UE:
                        if(i.getStatus() != StatusInscricao.Aberta){
                            Auditoria.logAviso("Tentou editar inscrição " + id + " com status " + i.getStatus());
                            return;
                        }
                        break;
                    case SME:
                        if(i.getStatus() != StatusInscricao.RecebidaSME && i.getStatus() != StatusInscricao.Alocada){
                            Auditoria.logAviso("Tentou editar inscrição " + id + " com status " + i.getStatus());
                            return;
                        }
                        break;
                    default:
                        return;
            }
            
            Gson gson = FornecedorGson.getGson();

            JsonElement dadosEle = gson.fromJson(Validacao.validar(request,"dados"), JsonElement.class);
            JsonObject dadosJson = dadosEle.getAsJsonObject();

            JsonElement enderecoEle = gson.fromJson(Validacao.validar(request,"endereco"), JsonElement.class);
            JsonObject endJson = enderecoEle.getAsJsonObject();

            JsonElement contatosEle = gson.fromJson(Validacao.validar(request,"contatos"), JsonElement.class);
            JsonObject conJson = contatosEle.getAsJsonObject();

            JsonElement documentosEle = gson.fromJson(Validacao.validar(request,"documentos"), JsonElement.class);
            JsonObject docJson = documentosEle.getAsJsonObject();


            //Atualizar dados e endereco
            //Recadastrar documentos e contatos pois sua quantidade pode mudar.


            DadosAluno dados = gson.fromJson(dadosEle, DadosAluno.class);
            dados.setId(i.getDados().getId());
            DadosAlunoDAO dadosDAO = new DadosAlunoDAO();
            dadosDAO.atualizar(dados);
            
            Endereco e = gson.fromJson(enderecoEle, Endereco.class);
            BairroDAO bDAO = new BairroDAO();
            e.setBairro(bDAO.consultar(endJson.get("id_bairro").getAsInt()));
            e.setDados(dados);
            e.setId(i.getDados().getEndereco().getId());
            EnderecoDAO eDAO = new EnderecoDAO();
            eDAO.atualizar(e);

            TipoDocumentoDAO tdDAO = new TipoDocumentoDAO();
            DocumentoDAO dDAO = new DocumentoDAO();
            
            for(Documento d: dDAO.listarPorDados(dados.getId())){
                dDAO.excluir(d.getId());
            }
            
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
                
                d.setId(dDAO.cadastrar(d));

                iDoc++;
            }
            
            TipoContatoDAO tcDAO = new TipoContatoDAO();
            ContatoDAO cDAO = new ContatoDAO();
            
            for(Contato c: cDAO.listarPorDados(dados.getId())){
                cDAO.excluir(c.getId());
            }

            int iCon = 1;
            while (conJson.get("tipoContato" + iCon) != null) {

                Contato c = new Contato();

                
                TipoContato t = tcDAO.consultar(conJson.get("tipoContato" + iCon).getAsInt());
                c.setTipoContato(t);

                c.setDado(conJson.get("contato" + iCon).getAsString());

                c.setDados(dados);

                c.setId(cDAO.cadastrar(c));

                iCon++;
            }

            i.setDados(dados);
            
            iDAO.atualizar(i);

            Auditoria.logInfo("Atualizou inscrição " + i.getStatus() + " " + id);
            
            out.print(Configuracao.getContextoAtual());
            
        } catch (Exception e){
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
