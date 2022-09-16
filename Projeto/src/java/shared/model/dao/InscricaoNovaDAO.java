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
import shared.model.Inscricao;
import shared.model.ObjetoDB;

/**
 *
 * @author leona
 */
public class InscricaoNovaDAO extends DAOGenerico{
    
    //usado apenas para cadastrar novas inscricoes
    
    public InscricaoNovaDAO() {
        super();
    }

    public InscricaoNovaDAO(Connection conManual) {
        super(conManual);
    }
    
    @Override
    protected void carregarComandoAtualizar(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    protected ObjetoDB gerarObjeto(ResultSet resultado) throws SQLException, ClassNotFoundException {
        throw new UnsupportedOperationException("Not supported.");
    }
    
    protected void carregarComando(PreparedStatement comando, ObjetoDB obj) throws SQLException {
        Inscricao objTipado = (Inscricao) obj;

        comando.setInt(1, objTipado.getEscolaOriginal().getId());
        
        comando.setInt(2, objTipado.getDados().getId());
        comando.setInt(3, objTipado.getStatus().valor);

    }
    
    protected static String INSERT = "insert into inscricao (id_escola_original, id_dados, status) values(?,?,?)";
    
    public int cadastrar(Inscricao obj) {
        return cadastrarGenerico(obj, INSERT);
    }
 
}
