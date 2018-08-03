/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntconsult.devtest.entities;

import java.io.File;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportTest {

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    public void testGenerateRerport() throws IOException {
        Resource data = resourceLoader.getResource("classpath:data.dat");

        File f = data.getFile();
        Report r = new Report();
        r.addFile(f);

        assertThat(r.getNumberOfSellers(), equalTo(6));
        assertThat(r.getNumberOfClients(), equalTo(4));
        assertThat(r.getWorstSellerName(), equalTo("Renato Aragao"));
        assertThat(r.getMostExpensiveSale().getId(), equalTo(8L));
    }

    @Test
    public void testGenerateRerportWithErrorFile() throws IOException {
        Resource data = resourceLoader.getResource("classpath:errorfile.dat");

        File f = data.getFile();
        Report r = new Report();
        r.addFile(f);

        assertThat(r.getNumberOfSellers(), equalTo(1));
        assertThat(r.getNumberOfClients(), equalTo(0));
        assertThat(r.getWorstSellerName(), equalTo("Renato"));
        assertThat(r.getMostExpensiveSale().getId(), equalTo(8L));
    }
}
