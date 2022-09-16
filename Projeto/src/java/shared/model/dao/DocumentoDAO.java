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
import shared.model.DadosAluno;
import shared.model.ObjetoDB;
import shared.model.Documento;

/**
 *
 * @author leona
 */
public class DocumentoDAO extends DAOGenerico {
    
    public DocumentoDAO() {
        super();
    }

    public DocumentoDAO(Connection conManual) {
        super(conManual);
    }

    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Documento objTipado = (Documento) obj;

        comando.setInt(1, objTipado.getTipoDocumento().getId());
        comando.setString(2, objTipado.getDado());
        if (objTipado.getValidade() != null) {
            comando.setDate(3, java.sql.Date.valueOf(objTipado.getValidade()));
        } else {
            comando.setDate(3, null);
        }
        comando.setInt(4, objTipado.getDados().getId());

    }

    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        comando.setInt(5, obj.getId());
    }

    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        Documento obj = new Documento(resultado.getInt("id"));

        TipoDocumentoDAO tDAO = new TipoDocumentoDAO();
        obj.setTipoDocumento(tDAO.consultar(resultado.getInt("id_tipoDocumento")));

        obj.setDado(resultado.getString("dado"));
        if (resultado.getDate("validade") != null) {
            obj.setValidade(resultado.getDate("validade").toLocalDate());
        }
        
        //setar dados?
        DadosAluno dados = new DadosAluno();
        dados.setId(resultado.getInt("id_dados"));
        obj.setDados(dados);
        
        return obj;
    }

    protected static String INSERT = "insert into documento (id_tipoDocumento, dado, validade, id_dados) values(?,?,?,?)";
    protected static String UPDATE = "update documento set id_tipoDocumento = ?, dado = ?, validade = ?, id_dados = ? where id = ?";
    protected static String DELETE = "delete from documento where id = ?";
    protected static String SELECT_ALL = "select * from documento";
    protected static String SELECT = "select * from documento where id = ?";

    public int cadastrar(Documento obj){
        return cadastrarGenerico(obj, INSERT);
    }

    public boolean atualizar(Documento obj){
        return atualizarGenerico(obj, UPDATE);
    }

    public boolean excluir(int id){
        return excluirGenerico(id, DELETE);
    }

    public List<Documento> listar(){
        return (List<Documento>) listarGenerico(SELECT_ALL);
    }

    public Documento consultar(int id){
        return (Documento) consultarGenerico(SELECT, id);
    }

    protected static String SELECT_DADOS = "select * from documento where id_dados = ?";

    public List<Documento> listarPorDados(int id_dados){

        ParametrosConsulta param = new ParametrosConsulta() {
            public void adicionarParametros(PreparedStatement comando) {
                try {
                    comando.setInt(1, id_dados);
                } catch (Exception e) {

                }
            }
        };
        return (List<Documento>) listarGenerico(SELECT_DADOS, param);

    }

}
