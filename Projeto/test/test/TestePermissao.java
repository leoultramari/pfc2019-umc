/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import shared.model.Usuario;
import shared.model.dao.UsuarioDAO;

/**
 *
 * @author leona
 */
public class TestePermissao {

    public static void main(String[] args) {
        try {

            UsuarioDAO uDAO = new UsuarioDAO();
            Usuario u = uDAO.consultar(3);

            java.lang.reflect.Method metodoPermissao = u.getCargo().getClass().getMethod("isPermVisualizarInscricoes");
            
            
            //System.out.println(Boolean.getBoolean(metodoPermissao.invoke(u.getCargo()).toString()));
            System.out.println(u.getCargo().getNome());
            System.out.println(metodoPermissao.invoke(u.getCargo()).toString());
            System.out.println(Boolean.parseBoolean(metodoPermissao.invoke(u.getCargo()).toString()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
