/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import shared.model.TipoMensagem;
import shared.model.dao.EscolaDAO;
import sme.model.CredencialEscola;
import sme.model.dao.CredencialEscolaDAO;

/**
 *
 * @author superalunocmc
 */
public class AutenticacaoMensagem {

    //true se a escola está passando por erros de autenticação
    //Deveria existir apenas na SME
    private static boolean[] status;

    private static void inicializarStatus() {
        try {
            if (status == null) {
                EscolaDAO eDAO = new EscolaDAO();
                int tamanho = eDAO.consultarMaiorId();
                Auditoria.logDepurar("Iniciando gerenciador da autenticação para " + tamanho + " escolas");
                //id 0 não será usado
                status = new boolean[tamanho + 1];
            }
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    private static boolean getStatus(int id) {
        if (status == null) {
            inicializarStatus();
        }
        return status[id];
    }

    private static void setStatus(int id, boolean s) {
        if (status == null) {
            inicializarStatus();
        }
        status[id] = s;
    }

    private static void atualizarErro(int id, boolean statusAtual) {
        try {
            //enviar apenas se o estado for diferente do atual para evitar spam de mensagens
            //necessário fazer spam de mensagens pois a escola pode reiniciar o sistema
            //if (getStatus(id) != statusAtual) {

                Session session = GerenciadorActiveMQ.getSession();
                TextMessage mensagem;
                if(statusAtual == true){
                    mensagem = session.createTextMessage("Erro de autenticação, contate o administrador do sistema.");
                } else {
                    mensagem = session.createTextMessage();
                }
                mensagem.setIntProperty("tipo", TipoMensagem.Erro.valor);
                mensagem.setJMSPriority(5);

                ProdutorJMS.produzir("escola" + id, mensagem);

                setStatus(id, statusAtual);
                Auditoria.logDepurar("Status de autenticação enviado para escola " + id);
            //}
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    public static void assinar(Message message) {
        try {
            message.setIntProperty("id", Configuracao.getInt("autenticacao", "id"));
            message.setStringProperty("login", Configuracao.get("autenticacao", "login"));
            message.setStringProperty("senha", Configuracao.get("autenticacao", "senha"));
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    public static boolean autenticar(Message message) {
        try {

            String idStr = message.getStringProperty("id");
            if (idStr == null || idStr.equals("")) {
                Auditoria.logAviso("Recebeu verificação inválida na mensagem");
                return false;
            }

            CredencialEscola c = new CredencialEscola();
            CredencialEscolaDAO cDAO = new CredencialEscolaDAO();

            c.setLogin(message.getStringProperty("login"));
            c.setSenha(message.getStringProperty("senha"));

            CredencialEscola cAuth = cDAO.autenticar(c);

            if (cAuth == null || cAuth.getId() != message.getIntProperty("id")) {
                atualizarErro(message.getIntProperty("id") , true);
                Auditoria.logAviso("Autenticação inválida na mensagem");
                return false;
            } else {
                //if (cAuth.getId() == message.getIntProperty("id")) {
                atualizarErro(message.getIntProperty("id") , false);
                Auditoria.logDepurar2("Mensagem autenticada");
                setStatus(message.getIntProperty("id"), false);
                return true;
            }

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
        return false;
    }

}
