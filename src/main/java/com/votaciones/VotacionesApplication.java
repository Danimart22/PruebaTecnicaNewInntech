package com.votaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class VotacionesApplication {

    // Inicia la aplicación Spring Boot
    public static void main(String[] args) {
        SpringApplication.run(VotacionesApplication.class, args);
    }
}