/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.time.LocalDate;
import java.time.Period;

/**
 *
 * @author leona
 */
public class TesteDataNascimento {

    public static int calcularIdade(LocalDate dataNasc) {
        //alterar de acordo com regra de calcular a idade?
        LocalDate dataAtual = LocalDate.now();
        if (dataNasc != null) {
            return Period.between(dataNasc, dataAtual).getYears();
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        System.out.println(calcularIdade(LocalDate.of(1998, 8, 7)));
        System.out.println(calcularIdade(LocalDate.of(2000, 8, 1)));
    }

}
