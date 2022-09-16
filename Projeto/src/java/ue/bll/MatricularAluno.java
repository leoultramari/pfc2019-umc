/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.PrintWriter;
import javax.jms.Session;
import javax.jms.TextMessage;
import shared.model.Aluno;
import shared.model.DadosAluno;
import shared.model.Inscricao;
import shared.model.StatusAluno;
import shared.model.StatusInscricao;
import shared.model.TipoMensagem;
import shared.model.dao.AlunoDAO;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;
import shared.util.AutenticacaoMensagem;
import shared.util.Configuracao;
import shared.util.FornecedorGson;
import shared.util.GerenciadorActiveMQ;
import shared.util.IntegridadeMensagem;
import shared.util.ProdutorJMS;

/**
 *
 * @author leona
 */
public class MatricularAluno {

    public static void matricular(int id) {
        try {

            InscricaoDAO iDAO = new InscricaoDAO();
            Inscricao i = iDAO.consultar(id);

            if (i == null) {
                Auditoria.logErro("Erro: Tentando matricular inscrição inválida");
                return;
            }

            if (i.getEscolaAlocada().getId() != Configuracao.getInt("autenticacao", "id")) {
                Auditoria.logErro("Erro: Tentando matricular inscrição de outra UE");
                return;
            }

            if (i.getStatus() != StatusInscricao.RecebidaUE) {
                Auditoria.logErro("Erro: Tentando matricular inscrição com status inválido");
                return;
            }

            AlunoDAO aDAO = new AlunoDAO();
            DadosAluno d = i.getDados();

            Aluno a = new Aluno();
            a.setDados(d);
            a.setStatus(StatusAluno.Matriculado);
            a.setIdEscolaOriginal(i.getEscolaOriginal().getId());
            a.setIdInscricao(i.getIdInscricao());

            int idAluno = aDAO.cadastrar(a);

            i.setStatus(StatusInscricao.Completa);
            if (iDAO.atualizar(i)) {
                Auditoria.logInfo("Completou inscrição " + i.imprimirId());
            }

            Auditoria.logInfo("Cadastrou aluno " + idAluno + " a partir da inscrição " + i.imprimirId());

            Session session = GerenciadorActiveMQ.getSession();
            if (session == null) {
                Auditoria.logAviso("ActiveMQ indisponível, confirmação de matrícula não enviada");
                return;
            }

            Gson gson = FornecedorGson.getGson();
            JsonObject dados = new JsonObject();
            dados.addProperty("idEscolaOriginal", a.getIdEscolaOriginal());
            dados.addProperty("idInscricao", a.getIdInscricao());
            dados.addProperty("status", StatusAluno.Matriculado.valor);

            TextMessage resposta = session.createTextMessage(gson.toJson(dados));
            resposta.setIntProperty("tipo", TipoMensagem.Matriculas.valor);
            resposta.setJMSPriority(7);
            AutenticacaoMensagem.assinar(resposta);
            IntegridadeMensagem.gravarMD5(resposta);
            ProdutorJMS.produzir("sme", resposta);
            Auditoria.logInfo("Confirmação da matrícula " + idAluno + ", inscrição " + a.imprimirId() + " enviado para a SME");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

}
