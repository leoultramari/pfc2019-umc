/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alunocmc
 */
public class Auditoria {

    private static Logger logger;

    private static FileHandler handler;

    public static void iniciar() {

        try {

            if (handler == null) {
                //criar pasta, falha silencionsamente se já existe
                new File("./logs").mkdirs();

                //novo log para cada execução
                LocalDateTime horario = LocalDateTime.now();

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
                String nomeLog = "log-" + horario.format(dtf) + ".txt";

                handler = new FileHandler("./logs/" + nomeLog, false);
                handler.setFormatter(new FormatadorAuditoria());

                //singleton?
                if (logger == null) {
                    logger = Logger.getLogger("Auditoria");
                }

                logger.addHandler(handler);

                log(Level.INFO, "Iniciando log " + nomeLog);
                
                logger.setLevel(Level.parse(Configuracao.get("geral", "auditoria")));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //para exceções
    public static void log(Level nivel, Exception e) {
        if (logger != null) {
            logger.log(nivel, e.getMessage(), e);
        }else {
            //para testes que não iniciam Auditoria
            e.printStackTrace();
        }
    }

    //para mensagens
    public static void log(Level nivel, String mensagem) {
        if (logger != null) {
            logger.log(nivel, mensagem);
        } else {
            //para testes que não iniciam Auditoria
            System.out.println(nivel + " - " + mensagem);
        }
    }

    public static void logInfo(String mensagem) {
        log(Level.INFO, mensagem);
    }

    public static void logInfo(Exception e) {
        log(Level.INFO, e);
    }

    public static void logConfig(String mensagem) {
        log(Level.CONFIG, mensagem);
    }

    public static void logConfig(Exception e) {
        log(Level.CONFIG, e);
    }

    public static void logDepurar(String mensagem) {
        log(Level.FINE, mensagem);
    }

    public static void logDepurar(Exception e) {
        log(Level.FINE, e);
    }

    public static void logDepurar2(String mensagem) {
        log(Level.FINER, mensagem);
    }

    public static void logDepurar2(Exception e) {
        log(Level.FINER, e);
    }

    public static void logDepurar3(String mensagem) {
        log(Level.FINEST, mensagem);
    }

    public static void logDepurar3(Exception e) {
        log(Level.FINEST, e);
    }

    public static void logAviso(String mensagem) {
        log(Level.WARNING, mensagem);
    }

    public static void logAviso(Exception e) {
        log(Level.WARNING, e);
    }

    public static void logErro(String mensagem) {
        log(Level.SEVERE, mensagem);
    }

    public static void logErro(Exception e) {
        log(Level.SEVERE, e);
    }

}
