/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import javax.servlet.http.HttpServletRequest;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 *
 * @author leona
 */
public class Validacao {

    private static String processarString(String str) {
        str = escapeHtml4(str);
        //necessário manter " para json
        str = str.replace("&quot;", "\"");
        //Auditoria.logInfo(str);
        return str;
    }

    public static String validarString(String str){
        return processarString(str);     
    }
    
    public static String validar(HttpServletRequest request, String parameter) {
        if (request.getParameter(parameter) == null) {
            Auditoria.logAviso("Recebeu entrada nula para " + parameter);
            return null;
        }

        return processarString(request.getParameter(parameter));
    }

    public static int validarInt(HttpServletRequest request, String parameter) {

        String str = validar(request, parameter);
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            Auditoria.logAviso("Recebeu entrada de número para" + parameter + " inválida " + str);
            return 0;
        }

    }
}
