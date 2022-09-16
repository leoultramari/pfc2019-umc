/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shared.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 *
 * @author leona
 */
public class TesteSelenium {

    public static WebDriver driver;

    @BeforeClass
    public static void iniciarWebDriver() {
        System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void finalizarWebDriver() {
        driver.quit();
    }

    public void tentarLogoff() {
        driver.get("http://localhost:8080/RealizarLogoff");
    }

    public void tentarLogin(String login, String senha) {
        tentarLogoff();

        driver.get("http://localhost:8080/login.html");

        WebElement txtLogin = driver.findElement(By.name("usuario"));
        WebElement txtSenha = driver.findElement(By.name("senha"));

        txtLogin.sendKeys(login);
        txtSenha.sendKeys(senha);

        WebElement btnLogin = driver.findElement(By.id("btnSubmit"));
        btnLogin.click();
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            Auditoria.logErro(e);
        }
    }

}
