/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntconsult.devtest.entities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;

/**
 *
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
public class SaleTest {

    @Test
    public void testGetSaleFromArray(){
        String[] sale = {"003","10","[1-10-100,2-30-2.50,3-40-3.10]","Diego"};
        Sale result = Sale.fromArray(sale);

        assertThat(result.getId(), equalTo(10L));
        assertThat(result.getSeller(), equalTo("Diego"));
        assertThat(result.getAmount(), equalTo(105.60F));
    }
}
