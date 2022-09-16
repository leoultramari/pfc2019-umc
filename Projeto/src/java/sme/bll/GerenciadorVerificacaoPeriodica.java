/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sme.bll;

import shared.util.AutenticacaoMensagem;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.jms.Message;
import javax.jms.MessageListener;
import shared.model.TipoMensagem;
import shared.model.dao.EscolaDAO;
import shared.util.Auditoria;
import shared.util.ConsumidorJMS;


/**
 *
 * @author superalunocmc
 */
public class GerenciadorVerificacaoPeriodica {

    private static LocalDateTime[] status;

    private static MessageListener consumidorVerificacaoPeriodica = new MessageListener() {

        @Override
        public void onMessage(Message message) {
            try {
                if (!AutenticacaoMensagem.autenticar(message)) {
                    return;
                }

                //int id = Integer.valueOf(idStr);
                int id = message.getIntProperty("id");

                //timestamp = data enviada ou data recebida?
                //dependente do fuso horário do sistema
                LocalDateTime data = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getJMSTimestamp()), ZoneId.systemDefault());
                status[id] = data;

                Auditoria.logDepurar("Recebeu verificação da escola " + id + " na data " + data);
            } catch (Exception e) {
                Auditoria.logErro(e);
            }
        }

    };

    public static LocalDateTime getStatus(int id_escola) {
        try {
            return status[id_escola];
        } catch (NullPointerException e) {
            Auditoria.logDepurar("Escola " + id_escola + " offline");
            return null;
        } catch (IndexOutOfBoundsException e) {
            Auditoria.logDepurar("Escola " + id_escola + " inválida");
            return null;
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
        return null;
    }

    public static void iniciar() {

        //System.out.println("Iniciando gerenciador da verificação periódica");
        try {

            EscolaDAO eDAO = new EscolaDAO();

            int tamanho = eDAO.consultarMaiorId();
            Auditoria.logInfo("Iniciando gerenciador da verificação periódica para " + tamanho + " escolas");
            //id 0 não será usado
            status = new LocalDateTime[tamanho + 1];

            String filtro = "tipo = " + TipoMensagem.Verificacao.valor;
            ConsumidorJMS.consumirFila("sme", consumidorVerificacaoPeriodica, filtro);

        } catch (Exception e) {
            Auditoria.logErro("Erro ao iniciar gerenciador da verificação periódica");
            Auditoria.logErro(e);
        }
    }

}
