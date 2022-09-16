/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leona
 */
public class GerenciadorFiltrosTest extends TesteSelenium {

    @Test
    public void testarFiltroSME() {
        tentarLogin("ue", "ue");
        driver.get("http://localhost:8080/SME/inscricao/lista-inscricoes.html");
        assertEquals("http://localhost:8080/login.html", driver.getCurrentUrl());
    }

    @Test
    public void testarFiltroUE() {
        tentarLogin("sme", "sme");
        driver.get("http://localhost:8080/UE/inscricao/lista-inscricoes.html");
        assertEquals("http://localhost:8080/login.html", driver.getCurrentUrl());
    }

}
