/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.util.concurrent.ScheduledExecutorService;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Web application lifecycle listener.
 *
 * @author superalunocmc
 */
public class AgendadorTarefas implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Auditoria.iniciar();
        
        Auditoria.logInfo("AgendadorTarefas inicializado");
        
        GerenciadorActiveMQ.iniciar();
        
        try {
            switch (Configuracao.getContextoAtual()) {
                case SME:
                    sme.bll.InicializacaoSME.inicializar();
                    break;
                case UE:
                    ue.bll.InicializacaoUE.inicializar();
                    break;
                default:
                    Auditoria.logErro("Nenhum contexto registrado, falha na inicialização");
                    break;
            }
        } catch (Exception e) {
            Auditoria.logErro("Falha na inicialização do AgendadorTarefas");
            Auditoria.logErro(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Auditoria.logInfo("AgendadorTarefas finalizado");
        
        try {
            switch (Configuracao.getContextoAtual()) {
                case SME:
                    sme.bll.InicializacaoSME.finalizar();
                    break;
                case UE:
                    ue.bll.InicializacaoUE.finalizar();
                    break;
                default:
                    Auditoria.logErro("Nenhum contexto registrado, falha na finalização");
                    break;
            }
        } catch (Exception e) {
            Auditoria.logErro("Falha na finalização do AgendadorTarefas");
            Auditoria.logErro(e);
        }
        
        
        
        GerenciadorActiveMQ.terminar();
        if (scheduler != null) {
            scheduler.shutdownNow();
            Auditoria.logDepurar("Terminando tarefas agendadas");
        }
    }
}
