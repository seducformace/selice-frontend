package com.config;

import com.security.JwtAuthenticationFilter;
import com.utils.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    // Filtro JWT: responsável por interceptar e validar o token
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            UserDetailsService userDetailsService,
            JwtTokenProvider jwtTokenProvider) {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    // Configura toda a segurança da aplicação
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            UserDetailsService userDetailsService
    ) throws Exception {
        logger.info("Configurando segurança HTTP...");

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/debug-user", // Corrigido: endpoint protegido anteriormente
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/error",
                                "/api/faculties",
                                "/api/faculties/**",
                                "/api/schools",
                                "/api/schools/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider(userDetailsService))
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("SecurityFilterChain configurado com sucesso.");
        return http.build();
    }

    // AuthenticationManager com provider padrão
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        logger.info("AuthenticationManager configurado com DaoAuthenticationProvider.");
        return new ProviderManager(authenticationProvider(userDetailsService));
    }

    // Provider para validação de credenciais
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        logger.info("Configurando AuthenticationProvider com UserDetailsService.");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Encoder de senhas: BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        logger.info("Utilizando BCryptPasswordEncoder.");
        return new BCryptPasswordEncoder();
    }

    // Configuração do CORS para permitir o frontend acessar a API
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        logger.info("Configurando CORS...");

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of(
                "http://localhost:8081", // Frontend Vue.js
                "http://localhost:4300"  // Frontend Angular
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        logger.info("CORS configurado corretamente.");
        return source;
    }
}
