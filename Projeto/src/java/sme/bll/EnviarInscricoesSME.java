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
import shared.model.Inscricao;
import shared.model.TipoMensagem;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;

/**
 *
 * @author leona
 */
public class EnviarInscricoesSME {

    public static void enviar() {
        try {

            Gson gson = FornecedorGson.getGson();
            JsonArray dados; //= new JsonArray();

            InscricaoDAO iDAO = new InscricaoDAO();
            ArrayList<Inscricao> lista = (ArrayList<Inscricao>) iDAO.listarAlocadas();

            if (lista.isEmpty()) {
                Auditoria.logInfo("Nenhuma inscrição para ser enviada");
                return;
            }

            Session session = GerenciadorActiveMQ.getSession();
            Auditoria.logInfo("Realizando envio de inscrições");

            //Inicialmente, enviar cada inscrição individualmente
            //Futuramente, criar pacotes de inscrições por escola
            //Dois grupos de pacotes: por escola original e por escola alocada
            for (Inscricao i : lista) {

                JsonObject iJson = gson.fromJson(gson.toJson(i), JsonObject.class);
                dados = new JsonArray();
                dados.add(iJson);

                String dadosTexto = dados.toString();

                TextMessage mensagem = session.createTextMessage(dadosTexto);

                mensagem.setIntProperty("tipo", TipoMensagem.Inscricoes.valor);
                mensagem.setJMSPriority(10);

                IntegridadeMensagem.gravarMD5(mensagem);

                int idOriginal = i.getEscolaOriginal().getId();
                int idAlocada = i.getEscolaAlocada().getId();

                Auditoria.logInfo("Enviando inscrição " + idOriginal + "-" + i.getIdInscricao() + " para escolas " + idOriginal + " e " + idAlocada);

                ProdutorJMS.produzir("escola" + idOriginal, mensagem);
                if (idOriginal != idAlocada) {
                    ProdutorJMS.produzir("escola" + idAlocada, mensagem);
                }

            }
            
            Auditoria.logInfo("Envio de inscrições finalizado");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

}
