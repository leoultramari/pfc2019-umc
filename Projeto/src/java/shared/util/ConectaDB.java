/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author leona
 */
public class ConectaDB {

    private static Connection conexaoTeste;

    public static Connection getConexao() throws ClassNotFoundException, SQLException {

        if (Configuracao.getTeste()) {
            return getConexaoTeste();
        }

        Class.forName("org.postgresql.Driver");

        String caminho = Configuracao.get("bancoProd", "caminho");
        String login = Configuracao.get("bancoProd", "login");
        String senha = Configuracao.get("bancoProd", "senha");
        Auditoria.logDepurar3("Usando banco de produção: " + caminho);

        Connection con = DriverManager.getConnection("jdbc:postgresql://" + caminho, login, senha);
        return con;
    }

    private static Connection getConexaoTeste() throws ClassNotFoundException, SQLException {

        if (conexaoTeste != null && !conexaoTeste.isClosed()) {
            Auditoria.logDepurar3("Usando conexão de teste existente");
            return conexaoTeste;
        }

        Class.forName("org.postgresql.Driver");

        String caminho = Configuracao.get("bancoTeste", "caminho");
        String login = Configuracao.get("bancoTeste", "login");
        String senha = Configuracao.get("bancoTeste", "senha");
        Auditoria.logDepurar3("Usando banco de teste: " + caminho);

        Connection con = DriverManager.getConnection("jdbc:postgresql://" + caminho, login, senha);
        Auditoria.logDepurar3("Criando nova conexão de teste");
        conexaoTeste = con;
        return conexaoTeste;

    }

}
