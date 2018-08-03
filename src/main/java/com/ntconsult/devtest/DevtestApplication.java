package com.ntconsult.devtest;

import com.ntconsult.devtest.services.FileService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DevtestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DevtestApplication.class, args);
        FileService fileService = context.getBean(FileService.class);
        fileService.watchFolder();
    }
}
