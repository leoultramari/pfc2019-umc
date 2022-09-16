/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import java.sql.SQLException;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import shared.model.Escola;
import shared.model.dao.EscolaDAO;
import shared.util.Auditoria;
import shared.util.GerenciadorActiveMQ;

/**
 *
 * @author superalunocmc
 */
public class InicializacaoSME {

    private static void criarQueues() throws ClassNotFoundException, SQLException, NamingException, JMSException {

        //O método createQueue não cria uma fila
        //Filas são criadas sob demanda devido a um comportamento especifíco do ActiveMQ
        //mas para isso ocorrer é necessário que exista um registro para a fila no JNDI
        Session session = GerenciadorActiveMQ.getSession();
        InitialContext context = GerenciadorActiveMQ.getContext();

        if (!GerenciadorActiveMQ.isOffline()) {
            Queue fila = session.createQueue("sme");
            context.rebind("sme", fila);

            EscolaDAO eDAO = new EscolaDAO();
            for (Escola e : eDAO.listar()) {

                String nomeFila = "escola" + e.getId();

                //criar entradas no JNDI
                //if (GerenciadorActiveMQ.tryLookup(nomeFila) == null) {
                Queue filaEscola = session.createQueue(nomeFila);
                context.rebind(nomeFila, filaEscola);
                //}

            }
        }

    }

    public static void inicializar() {

        Auditoria.logInfo("Inicializando SME");

        try {
            criarQueues();

            GerenciadorVerificacaoPeriodica.iniciar();
            ConsumidorInscricoesSME.iniciar();
            ConsumidorConfirmacaoEnvioInscricoesSME.iniciar();
            ConsumidorConfirmacaoEnvioFuncionarios.iniciar();
            ConsumidorMatriculasSME.iniciar();

        } catch (Exception e) {
            Auditoria.logErro("Erro ao inicializar SME");
            Auditoria.logErro(e);
        }

    }

    public static void finalizar() {

    }

}
