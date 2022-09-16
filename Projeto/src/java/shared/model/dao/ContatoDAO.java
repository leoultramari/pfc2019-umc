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
import java.util.List;
import shared.model.ObjetoDB;
import shared.model.Contato;
import shared.model.DadosAluno;

/**
 *
 * @author leona
 */
public class ContatoDAO extends DAOGenerico{
    
    public ContatoDAO() {
        super();
    }

    public ContatoDAO(Connection conManual) {
        super(conManual);
    }
    
    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Contato objTipado = (Contato) obj;

        comando.setInt(1, objTipado.getTipoContato().getId());
        comando.setString(2, objTipado.getDado());
        comando.setInt(3, objTipado.getDados().getId());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(4, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Contato obj = new Contato(resultado.getInt("id"));

        TipoContatoDAO tDAO = new TipoContatoDAO();
        obj.setTipoContato(tDAO.consultar(resultado.getInt("id_tipoContato")));
        
        obj.setDado(resultado.getString("dado"));
        
        //setar dadosAluno?
        DadosAluno dados = new DadosAluno();
        dados.setId(resultado.getInt("id_dados"));
        obj.setDados(dados);

        return obj;
    }

    protected static String INSERT = "insert into contato (id_tipoContato, dado, id_dados) values(?,?,?)";
    protected static String UPDATE = "update contato set id_tipoContato = ?, dado = ?, id_dados = ? where id = ?";
    protected static String DELETE = "delete from contato where id = ?";
    protected static String SELECT_ALL = "select * from contato";
    protected static String SELECT = "select * from contato where id = ?";

    public int cadastrar(Contato obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Contato obj){
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<Contato> listar(){
        return (List<Contato>) listarGenerico(SELECT_ALL);
    }

    public Contato consultar(int id){
        return (Contato) consultarGenerico(SELECT, id);
    }
    
    protected static String SELECT_DADOS = "select * from contato where id_dados = ?";

    public List<Contato> listarPorDados(int id_dados){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_dados);
                } catch (Exception e) {

                }
            }
        };
        return (List<Contato>) listarGenerico(SELECT_DADOS, param);

    }
    
}
