/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.google.gson.Gson;
import shared.model.dao.InscricaoDAO;

/**
 *
 * @author leona
 */
public class TestePerformance {

    public static void main(String[] args) {
        InscricaoDAO DAO = new InscricaoDAO();
        Gson gson = new Gson();

        System.out.print(gson.toJson(DAO.listarAbertas()));
    }

}
