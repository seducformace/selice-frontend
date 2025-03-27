package com;

import com.controller.middleware.AuthMiddleware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthMiddleware authMiddleware;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authMiddleware)
                .addPathPatterns("/**") // Intercepta todas as rotas
                .excludePathPatterns("/login", "/error", "/public/**"); // Exceções
    }
}
