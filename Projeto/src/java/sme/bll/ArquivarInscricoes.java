/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import java.util.ArrayList;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;

/**
 *
 * @author leona
 */
public class ArquivarInscricoes {
    
    public static void arquivar(){
        try{
        
            InscricaoDAO iDAO = new InscricaoDAO();
            ArrayList<Inscricao> lista = (ArrayList<Inscricao>) iDAO.listarEnviadasSME();
            
            if(lista.isEmpty()){
                Auditoria.logInfo("Nenhuma inscrição para arquivar");
                return;
            }
            
            String ids = "";
            for(Inscricao i: lista){

                i.setStatus(StatusInscricao.Arquivada);
                iDAO.atualizar(i);
                
                ids += i.imprimirId() + ", ";
                
            }
            Auditoria.logInfo("Arquivou inscrições " + ids);
            
        }catch(Exception e){
            Auditoria.logErro(e);
        }
    }
    
}
