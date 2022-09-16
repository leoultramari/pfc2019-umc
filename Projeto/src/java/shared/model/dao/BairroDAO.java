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
import shared.model.Bairro;
import shared.model.ObjetoDB;

/**
 *
 * @author superalunocmc
 */
public class BairroDAO extends DAOGenerico {
    
    public BairroDAO() {
        super();
    }

    public BairroDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Bairro objTipado = (Bairro) obj;

        comando.setString(1, objTipado.getNome());
        //carregar escola
        comando.setInt(2, objTipado.getEscola().getId());
    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(3, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Bairro obj = new Bairro(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));
        
        EscolaDAO eDAO = new EscolaDAO();
        obj.setEscola(eDAO.consultar(resultado.getInt("id_escola")));
        
        return obj;
    }
    
    protected ObjetoDB gerarObjetoSimples(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Bairro obj = new Bairro(resultado.getInt("id"));

        obj.setNome(resultado.getString("nome"));
        return obj;
    }

    protected static String INSERT = "insert into bairro (nome, id_escola) values(?,?)";
    public int cadastrar(Bairro obj){
        return cadastrarGenerico(obj, INSERT);       
    }
    protected static String UPDATE = "update bairro set nome = ?, id_escola = ? where id = ?";

    public boolean atualizar(Bairro obj){
        return atualizarGenerico(obj, UPDATE);
    }

    protected static String DELETE = "delete from bairro where id = ?";
    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }
    protected static String SELECT_ALL = "select * from bairro";

    public List<Bairro> listar(){
        return (List<Bairro>) listarGenerico(SELECT_ALL);
    }
    
    public List<Bairro> listarSimples(){
        return (List<Bairro>) listarGenerico(SELECT_ALL, null, true);
    }

    protected static String SELECT_ESCOLA = "select * from bairro where id_escola = ?";

    public List<Bairro> listarPorEscola(int id_escola){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_escola);
                } catch (Exception e) {

                }
            }
        };
        return (List<Bairro>) listarGenerico(SELECT_ESCOLA, param);

    }

    protected static String SELECT = "select * from bairro where id = ?";

    public Bairro consultar(int id){
        return (Bairro) consultarGenerico(SELECT, id);
    }

}
