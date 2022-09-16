/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.controller;

import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import shared.util.TesteSelenium;

/**
 *
 * @author leona
 */
public class RealizarLoginTest extends TesteSelenium {

    @Test
    public void verificarLoginSME() {
        tentarLogin("sme", "sme");
        assertEquals("http://localhost:8080/index.html", driver.getCurrentUrl());
    }

    @Test
    public void verificarLoginUE() {
        tentarLogin("ue", "ue");
        assertEquals("http://localhost:8080/index.html", driver.getCurrentUrl());
    }
    
    @Test
    public void verificarLoginAdmin() {
        tentarLogin("admin", "admin");
        assertEquals("http://localhost:8080/index.html", driver.getCurrentUrl());
    }
    
}
