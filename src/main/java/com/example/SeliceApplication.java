package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Classe principal do sistema SELICE.
 * Esta classe inicializa a aplicação Spring Boot e configura os pacotes necessários.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
		"com.controller",
		"com.service",
		"com.repository",
		"com.model",
		"com.config",
		"com.utils" // Adicionado para garantir que JwtTokenProvider seja registrado.
})
@EnableJpaRepositories(basePackages = "com.repository")
@EntityScan(basePackages = "com.model")
public class SeliceApplication {
	public static void main(String[] args) {
		// Inicia a aplicação Spring Boot
		SpringApplication.run(SeliceApplication.class, args);
	}
}
