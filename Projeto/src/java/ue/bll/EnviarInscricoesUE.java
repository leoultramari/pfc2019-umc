/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import javax.jms.Session;
import javax.jms.TextMessage;
import shared.model.Inscricao;
import shared.model.TipoMensagem;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;

/**
 *
 * @author leona
 */
public class EnviarInscricoesUE {

    public static void enviar() {

        try {

            Gson gson = FornecedorGson.getGson();
            //JsonObject dados = new JsonObject();
            JsonArray dados = new JsonArray();

            InscricaoDAO iDAO = new InscricaoDAO();
            ArrayList<Inscricao> lista = (ArrayList<Inscricao>) iDAO.listarAbertasCompletas();

            if (lista.isEmpty()) {
                Auditoria.logInfo("Nenhuma inscrição para ser enviada");
                return;
            }

            for (Inscricao i : lista) {

                JsonObject iJson = gson.fromJson(gson.toJson(i), JsonObject.class);
                dados.add(iJson);

            }

            String dadosTexto = dados.toString();

            //System.out.println(dadosTexto);           
            Auditoria.logInfo("Realizando envio de inscrições");

            Session session = GerenciadorActiveMQ.getSession();
            TextMessage mensagem = session.createTextMessage(dadosTexto);

            AutenticacaoMensagem.assinar(mensagem);

            mensagem.setIntProperty("tipo", TipoMensagem.Inscricoes.valor);
            mensagem.setJMSPriority(10);

            IntegridadeMensagem.gravarMD5(mensagem);

            ProdutorJMS.produzir("sme", mensagem);
            
            Auditoria.logInfo("Envio de inscrições finalizado");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

}
