/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import java.util.ArrayList;
import shared.model.Bairro;
import shared.model.Contato;
import shared.model.DadosAluno;
import shared.model.Escola;
import shared.model.Inscricao;
import shared.model.StatusInscricao;
import shared.model.dao.BairroDAO;
import shared.model.dao.EscolaDAO;
import shared.model.dao.InscricaoDAO;
import shared.util.Auditoria;

/**
 *
 * @author superalunocmc
 */
public class AlocarInscricoes {
    
    private static final String corpoEmail = "<html><body>"
            + "<h1>Caro %pai,</h1>"
            + "<h3>sua pré-inscrição para %nome, realizada na UE %escolaorig foi alocada para a UE</h3>"
            + "<h2>%escolaaloc</h2>"
            + "<h3>Para concluir a matrícula, por favor confirme em pessoa a inscrição na UE alocada.</h3>"
            + "</body></html>";

    public static void alocar() {

        Auditoria.logInfo("Realizando alocação de inscrições");

        try {

            InscricaoDAO iDAO = new InscricaoDAO();
            ArrayList<Inscricao> lista = (ArrayList<Inscricao>) iDAO.listarNaoAlocadas();

            if (lista.isEmpty()) {
                Auditoria.logInfo("Nenhuma inscrição para alocar");
                return;
            }

            BairroDAO bDAO = new BairroDAO();
            EscolaDAO eDAO = new EscolaDAO();

            for (Inscricao i : lista) {

                DadosAluno d = i.getDados();
                Bairro b = bDAO.consultar(d.getEndereco().getBairro().getId());
                Escola e = eDAO.consultar(b.getEscola().getId());

                i.setEscolaAlocada(e);
                i.setStatus(StatusInscricao.Alocada);
                iDAO.atualizar(i);

                for (Contato c : d.getContatos()) {
                    String nomeTipo = c.getTipoContato().getNome();
                    //grosso, verifica se existe um email cadastrado
                    if(nomeTipo.equalsIgnoreCase("Email") || nomeTipo.equalsIgnoreCase("E-mail")){
                        
                        String corpoEmailCompleto = corpoEmail;
                        corpoEmailCompleto = corpoEmailCompleto.replace("%pai", d.getFiliacao1());
                        corpoEmailCompleto = corpoEmailCompleto.replace("%nome", d.getNome());
                        corpoEmailCompleto = corpoEmailCompleto.replace("%escolaorig", i.getEscolaOriginal().getNome());
                        corpoEmailCompleto = corpoEmailCompleto.replace("%escolaaloc", i.getEscolaAlocada().getNome());
                        GerenciadorEmail.enviarEmail(c.getDado(), "Sua pré-inscrição foi alocada", corpoEmailCompleto);
                        
                        //enviar apenas ao primeiro email encontrado
                        break;
                    }
                }

            }
            
            Auditoria.logInfo("Alocação de inscrições finalizada");

        } catch (Exception e) {
            Auditoria.logErro(e);
        }

    }

}
