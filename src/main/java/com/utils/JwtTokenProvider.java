package com.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Classe utilitária responsável pela geração, extração e validação
 * de tokens JWT no sistema SELICE.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private final Key secretKey;

    // Tempo de expiração em milissegundos (padrão: 1 dia)
    @Value("${jwt.expiration:86400000}")
    private long expirationTime;

    // Construtor que converte a chave secreta para uma Key segura
    public JwtTokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Extrai o token JWT do cabeçalho Authorization.
     * Espera no formato: "Bearer eyJhbGciOiJIUzI1NiJ9..."
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " do início
        }
        return null;
    }

    /**
     * Gera um token JWT com as informações básicas do usuário.
     *
     * @param email Email do usuário (usado como subject)
     * @param role  Papel do usuário (será incluído como claim)
     * @return Token JWT assinado
     */
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrai o e-mail (username) do token JWT.
     *
     * @param token Token JWT
     * @return Email extraído do campo subject
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Valida se o token é autêntico e não expirou.
     *
     * @param token Token JWT
     * @return true se válido, false se inválido
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {}", e.getMessage());
        } catch (JwtException e) {
            logger.warn("Erro ao validar token: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Método interno para extrair todas as claims (informações) do token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
