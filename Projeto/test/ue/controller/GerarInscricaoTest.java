/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ue.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import shared.util.TesteSelenium;
import static shared.util.TesteSelenium.driver;

/**
 *
 * @author leona
 */
public class GerarInscricaoTest extends TesteSelenium {

    @Test
    public void testarGerarInscricao() throws InterruptedException {
        tentarLogin("ue", "ue");
        driver.get("http://localhost:8080/inscricao/form-inscricao.html");

        //aguardar até 5 segundos para carregamento dos objetos na página
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        LocalDateTime horario = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss");
                
        String nomeTeste = "Teste " + horario.format(dtf);

        WebElement element;

        //dados básicos
        element = driver.findElement(By.name("nome"));
        element.sendKeys(nomeTeste);

        element = driver.findElement(By.name("dataNascimento"));
        element.sendKeys("05252012");

        element = driver.findElement(By.name("sexo"));
        element.click();

        element = driver.findElement(By.name("filiacao1"));
        element.sendKeys("Teste 1");

        element = driver.findElement(By.name("filiacao2"));
        element.sendKeys("Teste 2");

        //adicionar documentos
        element = driver.findElement(By.name("docType"));
        element.sendKeys("rg");
        element = driver.findElement(By.id("newDocument"));
        element.click();
        element = driver.findElement(By.name("documento1"));
        element.sendKeys("11111111");

        element = driver.findElement(By.name("docType"));
        element.sendKeys("cpf");
        element = driver.findElement(By.id("newDocument"));
        element.click();
        element = driver.findElement(By.name("documento2"));
        element.sendKeys("22222222");

        element = driver.findElement(By.name("docType"));
        element.sendKeys("cert");
        element = driver.findElement(By.id("newDocument"));
        element.click();
        element = driver.findElement(By.name("documento3"));
        element.sendKeys("33333333");

        //adicionar contatos
        element = driver.findElement(By.name("tipoContato"));
        element.sendKeys("tel");
        element = driver.findElement(By.id("addContato"));
        element.click();
        element = driver.findElement(By.id("contato1"));
        element.sendKeys("11111111");

        element = driver.findElement(By.name("tipoContato"));
        element.sendKeys("em");
        element = driver.findElement(By.id("addContato"));
        element.click();
        element = driver.findElement(By.id("contato2"));
        element.sendKeys("test@test.com");

        //endereco
        element = driver.findElement(By.name("cep"));
        element.sendKeys("07500000");

        element = driver.findElement(By.name("logradouro"));
        element.sendKeys("aaaaaaaaa");

        element = driver.findElement(By.name("num"));
        element.sendKeys("3");

        //seleciona bairro aleatório entre 1 e 5
        Random random = new Random();
        int i;
        int max = 1 + random.nextInt(5);        

        element = driver.findElement(By.id("bairro"));
        for (i = 0; i < max; i++ ) {
            element.sendKeys("b");
        }
        
        element = driver.findElement(By.id("submit"));
        element.click();

        //aguardar 5 segundos
        Thread.sleep(5000);
        //se houve redirecionamento, o formulário foi enviado com sucesso
        assertEquals("http://localhost:8080/UE/inscricao/lista-inscricoes.html",driver.getCurrentUrl());
        
        //verificar se foi cadastrada na lista
        //driver.get("http://localhost:8080/UE/inscricao/lista-inscricoes.html");               
        driver.findElement(By.xpath("//td[contains(text(), '"+ nomeTeste +"')]"));

    }

}
