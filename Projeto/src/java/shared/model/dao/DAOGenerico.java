/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import shared.model.ObjetoDB;
import shared.util.Auditoria;
import shared.util.ConectaDB;
import shared.util.Configuracao;

/**
 *
 * @author superalunocmc
 */
public abstract class DAOGenerico {

    protected Connection con = null;
    protected boolean usandoConexaoManual = false;

    protected abstract void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException;

    protected abstract void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException;

    protected abstract ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException;

    //Não pode ser abstrato, nem toda DAO irá implementar
    protected ObjetoDB gerarObjetoSimples(ResultSet resultado) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Connection getCon() {
        return con;
    }

    public DAOGenerico() {
        try {
            //não fechar conexões durante modo de teste
            if (Configuracao.getTeste()) {
                usandoConexaoManual = true;
            }
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    public DAOGenerico(Connection conexaoManual) {
        usandoConexaoManual = true;
        this.con = conexaoManual;
    }

    private void abrirConexao() {
        if (usandoConexaoManual) {
            return;
        }
        try {
            if (con != null && !con.isClosed()) {
                //con.close();
                return;
            }
            con = ConectaDB.getConexao();
        } catch (Exception e) {
            try {
                con.close();
            } catch (Exception e2) {
                Auditoria.logErro(e2);
            }
            Auditoria.logErro(e);
        }
    }

    private void fecharConexao() {
        if (usandoConexaoManual) {
            return;
        }
        try {
            if (!con.isClosed()) {
                con.close();
                con = null;
            }
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

    protected int cadastrarGenerico(ObjetoDB obj, String sql) {

        //Connection con = null;
        int id = 0;
        try {
            abrirConexao();

            PreparedStatement comando = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            Auditoria.logDepurar2("Cadastrando " + sql);

            carregarComando(comando, obj);
            comando.execute();

            ResultSet rsId = comando.getGeneratedKeys();
            if (rsId.next()) {
                id = rsId.getInt("id");
            }

            fecharConexao();
        } catch (Exception e) {
            Auditoria.logErro(e);
            fecharConexao();
        }

        return id;
    }

    protected boolean atualizarGenerico(ObjetoDB obj, String sql) {
        //Connection con = null;
        try {
            abrirConexao();

            PreparedStatement comando = con.prepareStatement(sql);
            Auditoria.logDepurar2("Atualizando " + sql);

            carregarComando(comando, obj);
            carregarComandoAtualizar(comando, obj);
            comando.execute();
            fecharConexao();
            return true;
        } catch (Exception e) {
            Auditoria.logErro(e);
            fecharConexao();
            return false;
        }
    }

    protected boolean excluirGenerico(int id, String sql) {
        //Connection con = null;
        try {
            abrirConexao();
            PreparedStatement comando = con.prepareStatement(sql);
            Auditoria.logDepurar2("Excluindo " + sql);

            comando.setInt(1, id);
            comando.execute();
            fecharConexao();
            return true;
        } catch (Exception e) {
            Auditoria.logErro(e);
            fecharConexao();
            return false;
        }
    }

    //listar todos
    protected List<? extends ObjetoDB> listarGenerico(String sql) {
        return listarGenerico(sql, null, false);
    }

    //lista customizada
    protected List<? extends ObjetoDB> listarGenerico(String sql, ParametrosConsulta consulta) {
        return listarGenerico(sql, consulta, false);
    }

    //lista customizada
    //possibilidade de uso de um metódo alternativo para gerar o objeto, que carrega-o com menos dados
    protected List<? extends ObjetoDB> listarGenerico(String sql, ParametrosConsulta consulta, boolean simples) {
        //Connection con = null;
        try {
            /*
            java.lang.reflect.Method metodoGerarObjeto = this.getClass().getMethod(nomeFuncao, ResultSet.class);
            if (metodoGerarObjeto == null) {
                System.out.println("Método gerarObjeto inválido em " + getClass());
            }
            ObjetoDB obj = (ObjetoDB) metodoGerarObjeto.invoke(resultado);
             */

            abrirConexao();
            PreparedStatement comando = con.prepareStatement(sql);
            Auditoria.logDepurar2("Listando " + sql);
            if (consulta != null) {
                consulta.adicionarParametros(comando);
            }
            ResultSet resultado = comando.executeQuery();

            List<ObjetoDB> todosObjetos = new ArrayList<ObjetoDB>();

            while (resultado.next()) {
                ObjetoDB obj = (simples) ? gerarObjetoSimples(resultado) : gerarObjeto(resultado);
                todosObjetos.add(obj);
            }
            fecharConexao();
            return todosObjetos;
        } catch (Exception e) {
            Auditoria.logErro(e);
            fecharConexao();
            return null;
        }
    }

    //busca por ID
    protected ObjetoDB consultarGenerico(String sql, int id) {
        return consultarGenerico(sql, id, false);
    }

    protected ObjetoDB consultarGenerico(String sql, int id, boolean simples) {
        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id);
                } catch (Exception e) {
                    Auditoria.logErro(e);
                }
            }
        };
        return consultarGenerico(sql, param, simples);
    }

    //busca customizada
    protected ObjetoDB consultarGenerico(String sql, ParametrosConsulta consulta) {
        return consultarGenerico(sql, consulta, false);
    }

    //busca customizada
    protected ObjetoDB consultarGenerico(String sql, ParametrosConsulta consulta, boolean simples) {
        //Connection con = null;
        try {
            abrirConexao();
            PreparedStatement comando = con.prepareStatement(sql);
            Auditoria.logDepurar2("Consultando " + sql);
            consulta.adicionarParametros(comando);
            ResultSet resultado = comando.executeQuery();

            ObjetoDB obj = null;

            while (resultado.next()) {
                obj = (simples) ? gerarObjetoSimples(resultado) : gerarObjeto(resultado);
            }
            fecharConexao();
            return obj;
        } catch (Exception e) {
            Auditoria.logErro(e);
            fecharConexao();
            return null;
        }
    }

    //count() customizado
    protected int contarGenerico(String sql, ParametrosConsulta consulta) {
        //Connection con = null;
        try {
            abrirConexao();
            PreparedStatement comando = con.prepareStatement(sql);
            Auditoria.logDepurar2("Contando " + sql);
            consulta.adicionarParametros(comando);
            ResultSet resultado = comando.executeQuery();

            int count = -1;

            while (resultado.next()) {
                count = resultado.getInt("count");
            }
            fecharConexao();
            return count;
        } catch (Exception e) {
            Auditoria.logErro(e);
            fecharConexao();
            return -1;
        }
    }

    //Proteção contra conexões abandonadas
    //Executado quando a DAO é coletada pelo GC
    protected void finalize() throws Throwable {
        try {
            con.close();
        } catch (SQLException e) {
            Auditoria.logErro(e);
        }
        super.finalize();
    }
    /*
    protected String adicionarOffset(String sql, int tamanho, int offset) {
        return sql + " ORDER BY id ASC LIMIT " + tamanho + " OFFSET " + offset;
    }
    */
}
