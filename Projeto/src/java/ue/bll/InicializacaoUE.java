/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.bll;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import shared.util.Auditoria;
import shared.util.Configuracao;
import shared.util.GerenciadorActiveMQ;

/**
 *
 * @author superalunocmc
 */
public class InicializacaoUE {

    private static ScheduledFuture<?> verificacaoPeriodica;

    private static void criarQueues() throws JMSException, NamingException {

        Session session = GerenciadorActiveMQ.getSession();
        InitialContext context = GerenciadorActiveMQ.getContext();

        if (!GerenciadorActiveMQ.isOffline()) {
            Queue filaSME = session.createQueue("sme");
            Auditoria.logDepurar("FILA SME : " + filaSME);
            context.rebind("sme", filaSME);

            String nomeFila = "escola" + Configuracao.get("autenticacao", "id");

            Queue filaUE = session.createQueue(nomeFila);
            Auditoria.logDepurar("FILA UE: " + filaUE);
            context.rebind(nomeFila, filaUE);
        }

    }

    private static void agendarVerificacao() {
        Auditoria.logInfo("Agendando verificação periódica");
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        verificacaoPeriodica = scheduler.scheduleAtFixedRate(new VerificacaoPeriodica(), 0, 1, TimeUnit.MINUTES);

        //atrasar execução inicial por 1 minuto
        //scheduler.scheduleAtFixedRate(new VerificacaoPeriodica(), 1, 1, TimeUnit.MINUTES);
    }

    public static void inicializar() {

        Auditoria.logInfo("Inicializando UE");

        try {
            criarQueues();

            agendarVerificacao();
            ConsumidorInscricoesUE.iniciar();
            ConsumidorConfirmacaoEnvioInscricoesUE.iniciar();
            ConsumidorFuncionarios.iniciar();
            ConsumidorErroUE.iniciar();

        } catch (Exception e) {
            Auditoria.logErro("Erro ao inicializar UE");
            Auditoria.logErro(e);
        }

    }

    public static void finalizar() {

        verificacaoPeriodica.cancel(false);
        Auditoria.logInfo("Removendo verificação periódica");

    }

}
