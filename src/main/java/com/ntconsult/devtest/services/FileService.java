/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ntconsult.devtest.services;

import com.ntconsult.devtest.entities.Report;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
@Service
public class FileService {

    @Value("${files.directory.in.absolutepath}")
    private String dataIn;

    @Value("${files.directory.out.absolutepath}")
    private String pathToSaveReport;

    @Value("${files.extension}")
    private String extensionFilter;

    @Value("${files.directory.out.resultfilename}")
    private String reportFileName;

    private final Logger log = LoggerFactory.getLogger(FileService.class);

    /**
     * Watch a folder and generate event to read files when a new file was created.
     */
    public void watchFolder() {
        //creating the folder if it doesn't exist.
        new File(dataIn).mkdirs();

        loadFiles();
        try {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                Path path = Paths.get(dataIn);

                path.register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_CREATE);

                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        String fileName = event.context().toString();
                        log.debug("New file was detected. File name: " + event.context());
                        if (fileName.endsWith(".dat")) {
                            //load the .dat files and gerenate a result done.dat
                            loadFiles();
                        }
                    }
                    key.reset();
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("Erro ao ler arquivos.", e);
        }
    }

    /**
     * Load all files from the folder with extension informed on application.properties.
     */
    public void loadFiles() {
        log.debug("Reading files and processing..");
        Report report = new Report();
        final File file = Paths.get(dataIn).toFile();

        Stream.of(file.listFiles((pFile, pString) -> pString.endsWith(extensionFilter)))
                .forEach(f -> {
                    //add the file to report
                    report.addFile(f);
                });

        writeReportFile(report);
    }

    private void writeReportFile(Report report) {
        // Creating/updating the report file
        log.info("Writing the the report file...");
        try {
            //creating the folder if it doesn't exist.
            new File(pathToSaveReport).mkdirs();

            String outputFileName = pathToSaveReport + "/" + reportFileName;
            File outputFile = new File(outputFileName);
            try (PrintWriter output = new PrintWriter(outputFile)) {
                output.println("Quantidade de clientes: " + report.getNumberOfClients());
                output.println("Quantidade de vendedores: " + report.getNumberOfSellers());
                output.println("ID da venda mais cara: " + report.getMostExpensiveSale().getId());
                output.println("Nome do pior vendedor: " + report.getWorstSellerName());
            }
        } catch (FileNotFoundException e) {
            log.error("There was an error saving the report file.", e);
        }
        log.info("Done.");
    }
}
