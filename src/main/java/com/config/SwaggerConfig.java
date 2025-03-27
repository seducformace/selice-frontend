package com.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger para documentação da API usando OpenAPI 3.0.
 * Ele cria uma interface interativa para visualizar e testar os endpoints.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Método responsável por configurar os detalhes básicos da documentação Swagger.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SELICE - Sistema de Estágio e Licenciatura do Ceará")
                        .version("1.0")
                        .description("Documentação interativa da API SELICE. Permite testar e visualizar os endpoints disponíveis.")
                );
    }
}
