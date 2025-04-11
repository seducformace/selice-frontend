package com;

import com.controller.middleware.AuthMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração global de CORS e interceptadores da aplicação.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthMiddleware authMiddleware;

    /**
     * Intercepta todas as requisições, exceto as públicas.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authMiddleware)
                .addPathPatterns("/**") // Aplica o middleware em todas as rotas
                .excludePathPatterns("/login", "/error", "/public/**"); // Rotas livres de autenticação
    }

    /**
     * Configura as permissões de CORS para aceitar requisições do frontend.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8081") // Permite somente o frontend local
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // Permite envio de cookies e headers como Authorization
    }
}
