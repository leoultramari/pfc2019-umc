/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.sql.ResultSet;
import shared.model.dao.BairroDAO;

/**
 *
 * @author superalunocmc
 */
public class TesteGetMethod {

    public static void main(String[] args) {
        try {

            BairroDAO bDAO = new BairroDAO();

            Class[] cArg = new Class[1];
            cArg[0] = ResultSet.class;

            //não pode acessar método protected ou private
            java.lang.reflect.Method metodoGerarObjeto = bDAO.getClass().getMethod("gerarObjetoSimples", cArg);

            System.out.println(metodoGerarObjeto);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
