package com.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.utils.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro responsável por interceptar requisições e autenticar usuários
 * com base no token JWT enviado no cabeçalho Authorization.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Método executado em cada requisição. Verifica se há um token válido
     * no cabeçalho Authorization e, se sim, autentica o usuário.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            // Extrai o token do cabeçalho Authorization
            String token = jwtTokenProvider.resolveToken(request);

            // Valida o token e garante que não há autenticação ativa no contexto
            if (token != null && jwtTokenProvider.validateToken(token)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Extrai o nome de usuário (geralmente e-mail)
                String username = jwtTokenProvider.extractUsername(token);

                // Busca os detalhes do usuário no banco de dados
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (userDetails != null) {
                    // Cria o objeto de autenticação
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    // Cria e define o contexto de segurança
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);

                    logger.debug("Usuário autenticado com sucesso: {}", username);
                } else {
                    logger.warn("Usuário não encontrado para o token fornecido.");
                }
            }

        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("Token malformado: {}", e.getMessage());
        } catch (SecurityException e) {
            logger.warn("Erro de segurança ao validar o token: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erro inesperado durante autenticação JWT: {}", e.getMessage(), e);
        }

        // Continua o fluxo da requisição normalmente
        chain.doFilter(request, response);
    }
}
