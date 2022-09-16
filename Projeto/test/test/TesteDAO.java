/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.SQLException;
import java.util.ArrayList;
import shared.model.Escola;
import shared.model.Bairro;
import shared.model.dao.EscolaDAO;

/**
 *
 * @author superalunocmc
 */
public class TesteDAO {
    
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
         
        EscolaDAO dao = new EscolaDAO();
        
        //Escola e = new Escola("Test", new ArrayList<Bairro>());
        //dao.cadastrar(e);  
        
        Escola e2 = new Escola(3);
        e2.setNome("Test 2");
        dao.atualizar(e2);
             
        //Escola e3 = dao.consultar(1);
        //System.out.println(e3.getNome());
        
        //dao.excluir(1);
        
    }
    
}
