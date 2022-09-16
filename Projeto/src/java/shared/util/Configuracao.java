/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.io.File;
import java.io.IOException;
import org.ini4j.Ini;
import shared.model.Contexto;

/**
 *
 * @author leona
 */
public class Configuracao {

    //mudar caminho do arquivo de configuração?
    private static File config = new File("config.ini");

    private static Ini ini;

    private static boolean testando = false;

    private static void carregar() {
        try {
            if (!config.exists()) {
                criarPadrao();
            } else {
                Auditoria.logInfo("Carregando configuração de " + config.getAbsolutePath());
                ini = new Ini(config);
            }

            //proteção contra configurações antigas
            //expandir
            if (ini.get("geral", "contexto") == null) {
                recriar();
                return;
            }

        } catch (IOException e) {
            Auditoria.logErro(e);
        }
    }

    private static void recriar() {
        Auditoria.logInfo("Recriando configuração");
        config.delete();
        criarPadrao();
    }

    //Deve ser chamado apenas se o arquivo config não existir
    private static void criarPadrao() {
        try {
            config.createNewFile();
            ini = new Ini(config);
            ini.getConfig().setEscape(false);

            ini.put("geral", "contexto", "UE");
            ini.put("geral", "auditoria", "INFO");

            ini.put("bancoProd", "caminho", "localhost:5432/dbTrab");
            ini.put("bancoProd", "login", "postgres");
            ini.put("bancoProd", "senha", "postgres");

            ini.put("bancoTeste", "caminho", "localhost:5432/dbTest");
            ini.put("bancoTeste", "login", "postgres");
            ini.put("bancoTeste", "senha", "postgres");

            ini.put("activeMQ", "endereco", "tcp://localhost:61616");
            //ini.put("activeMQ", "endereco", "vm://localhost?brokerConfig=xbean:activemq.xml");

            ini.put("autenticacao", "id", "1");
            ini.put("autenticacao", "login", "escola1");
            ini.put("autenticacao", "senha", "escola1");

            ini.put("turmas", "numSalas", "3");
            ini.put("turmas", "capacidadeSala", "20");
            ini.put("turmas", "tamanhoMinimo", "15");
            
            ini.put("turmas", "numSeries", "4");
            ini.put("turmas", "idade1", "6");

            ini.put("email", "ativado", "false");
            ini.put("email", "endereco", "test@test.com");
            ini.put("email", "senha", "test");
            ini.put("email", "host", "smtp.gmail.com");
            ini.put("email", "porta", "587");
            
            ini.put("teste", "qtd", "20");

            ini.store(config);

            Auditoria.logInfo("Configuração criada em " + config.getAbsolutePath());

            //carregar();
        } catch (IOException e) {
            Auditoria.logErro("Erro ao criar configuração");
            Auditoria.logErro(e);
        }
    }

    //Busca uma configuração do .ini
    //Carrega o .ini se ele ainda não foi carregado
    public static String get(String categoria, String propriedade) {
        if (ini == null) {
            carregar();
        }
        return ini.get(categoria, propriedade);
    }

    public static int getInt(String categoria, String propriedade) {
        if (ini == null) {
            carregar();
        }
        
        int valor = -1;
        
        try {
            valor = Integer.valueOf(ini.get(categoria, propriedade));
        } catch (Exception e) {
            Auditoria.logErro("Entrada de configuração vazia para [" + categoria + "] " + propriedade);
        }
        return valor;
    }

    public static Contexto getContextoAtual() {
        if (ini == null) {
            carregar();
        }
        return Contexto.fromString(ini.get("geral", "contexto"));
    }

    public static boolean getTeste() {
        return testando;
    }
    
    public static void iniciarTeste(){
        Auditoria.logDepurar("Iniciando modo de teste");
        testando = true;
    }
    
    public static void finalizarTeste(){
        Auditoria.logDepurar("Finalizando modo de teste");
        testando = false;
    }

}
