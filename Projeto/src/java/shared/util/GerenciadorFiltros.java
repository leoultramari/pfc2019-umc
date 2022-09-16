/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import shared.model.Contexto;
import shared.model.Filtro;
import shared.model.Usuario;
import shared.model.dao.FiltroDAO;
import shared.model.dao.PermissaoDAO;

/**
 *
 * @author leona
 */
@WebFilter(filterName = "GerenciadorFiltros", urlPatterns = {"/*"})
public class GerenciadorFiltros implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Auditoria.logInfo("Inicializando gerenciador de filtros");
    }
    
    public static boolean verificarFiltro(Filtro f, Usuario u) {
        List<Filtro> filtros = new ArrayList<Filtro>();
        filtros.add(f);
        return verificarFiltro(filtros, u);
    }

    public static boolean verificarFiltro(List<Filtro> filtros, Usuario u) {
        return verificarFiltro(filtros, u, "");
    }
    
    public static boolean verificarFiltro(Filtro f, HttpServletRequest request) {
        List<Filtro> filtros = new ArrayList<Filtro>();
        filtros.add(f);
        Usuario u = (Usuario) request.getSession().getAttribute("credencial");
        return verificarFiltro(filtros, u, request.getServletPath());
    }
    
    public static boolean verificarFiltro(List<Filtro> filtros, HttpServletRequest request) {
        Usuario u = (Usuario) request.getSession().getAttribute("credencial");
        return verificarFiltro(filtros, u, request.getServletPath());
    }

    public static boolean verificarFiltro(List<Filtro> filtros, Usuario u, String URL) {

        Auditoria.logDepurar("Filtro invocado para URL " + URL);
        
        String nomeUsuario = "<não logado>";
        if (u != null) {
            nomeUsuario = u.getLogin();
        }

        //System.out.println("Verificando " + filtros.size() + " filtros para usuario " + nomeUsuario);   
        PermissaoDAO pDAO = new PermissaoDAO();

        //iterar por todos os filtros da URL sendo acessada
        //se um filtro falhar, negar acesso
        for (Filtro f : filtros) {
            if (URL.equals("")) {
                URL = "Filtro " + f.getURL();
            }
            if (u == null && f.isRequerLogin()) {
                Auditoria.logAviso(URL + " - Acesso negado para usuário sem login");
                return false;
            }

            if (f.getContexto() != null) {
                //System.out.println("Filtro requer contexto " + f.getContexto() + ", contexto atual = " + Configuracao.getContextoAtual());
                if (Configuracao.getContextoAtual() != f.getContexto()) {
                    Auditoria.logAviso(URL + " - Acesso negado para " + nomeUsuario + " - Contexto de aplicação incorreto");
                    return false;
                }

                if (f.getContexto() == Contexto.SME) {
                    //Negar acesso a funções SME para usuários UE
                    if (u.getEscola() != null) {
                        Auditoria.logAviso(URL + " - Acesso negado para " + nomeUsuario + " - Contexto de usuário (UE) incorreto");
                        return false;
                    }
                }

                if (f.getContexto() == Contexto.UE) {
                    //Negar acesso a funções UE para funcionários SME
                    if (u.getEscola() == null) {
                        Auditoria.logAviso(URL + " - Acesso negado para " + nomeUsuario + " - Contexto de usuário (SME) incorreto");
                        return false;
                    }

                    //Negar acesso a funções UE para funcionários de outras UEs
                    if (u.getEscola().getId() != Configuracao.getInt("autenticacao", "id")) {
                        Auditoria.logAviso(URL + " - Acesso negado para " + nomeUsuario + " - UE incorreta");
                        return false;
                    }
                }

            }

            if (f.getTipoPermissao() != null) {
                if (pDAO.consultarParaCargo(u.getCargo().getId(), f.getTipoPermissao().getId()) == null) {
                    Auditoria.logAviso(URL + " - Acesso negado para " + nomeUsuario + " - Sem permissão");
                    return false;
                }
            }
        }

        if (filtros.size() > 0) {
            //imprimir mensagem apenas se pelo menos um filtro foi verificado
            Auditoria.logInfo(URL + " - Acesso permitido para " + nomeUsuario);
        }
        return true;
    }

    @Override
    public void doFilter(ServletRequest rawRequest, ServletResponse rawResponse, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) rawRequest;
        HttpServletResponse response = (HttpServletResponse) rawResponse;
        HttpSession session = request.getSession();

        String URL = request.getServletPath();
        //System.out.println("URL = " + URL);
        //System.out.println("URI = " + request.getRequestURI());

        Usuario usuarioLogado = (Usuario) session.getAttribute("credencial");

        FiltroDAO fDAO = new FiltroDAO();
        List<Filtro> filtros = fDAO.listarPorURL(URL);

        for (Filtro f : filtros) {
            Auditoria.logDepurar("Verificando filtro " + f.getId() + " - " + f.getURL());
        }

        boolean permitir = verificarFiltro(filtros, request);

        if (!permitir) {
            //response.sendError(401);
            response.sendRedirect("/login.html");
            //System.out.println("Usuario " + usuario + " negado acesso a URL " + URL);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Auditoria.logInfo("Finalizando gerenciador de filtros");
    }

}
