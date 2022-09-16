/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import javax.jms.Session;
import javax.jms.TextMessage;
import shared.model.Funcionario;
import shared.model.TipoMensagem;
import shared.model.dao.FuncionarioDAO;
import shared.util.Auditoria;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;

/**
 *
 * @author leona
 */
public class EnviarFuncionarios {
    
    public static void enviar() {
        try {

            Gson gson = FornecedorGson.getGson();
            JsonArray dados;

            FuncionarioDAO DAO = new FuncionarioDAO();
            ArrayList<Funcionario> lista = (ArrayList<Funcionario>) DAO.listarParaAtualizar();

            if (lista.isEmpty()) {
                Auditoria.logInfo("Nenhuma funcionário a ser enviado");
                return;
            }

            Session session = GerenciadorActiveMQ.getSession();
            Auditoria.logInfo("Realizando envio de funcionários");

            for (Funcionario obj : lista) {

                JsonObject iJson = gson.fromJson(gson.toJson(obj), JsonObject.class);
                dados = new JsonArray();
                dados.add(iJson);

                String dadosTexto = dados.toString();

                TextMessage mensagem = session.createTextMessage(dadosTexto);

                mensagem.setIntProperty("tipo", TipoMensagem.Funcionarios.valor);
                mensagem.setJMSPriority(9);

                IntegridadeMensagem.gravarMD5(mensagem);

                int idEscola = obj.getEscola().getId();

                Auditoria.logInfo("Enviando funcionário com registro " + obj.getRegistro() + " para escola " + idEscola);

                ProdutorJMS.produzir("escola" + idEscola, mensagem);

            }
            
            Auditoria.logInfo("Envio de funcionários finalizado");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }
    
}
