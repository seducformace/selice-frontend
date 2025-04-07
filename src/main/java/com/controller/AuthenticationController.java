package com.controller;

import com.utils.JwtTokenProvider;
import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.service.UserDetailsServiceImpl;
import com.model.User;
import com.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador responsável por autenticar usuários e gerar o token JWT.
 */
@RestController
@RequestMapping("/api/authentication")
@CrossOrigin(origins = "http://localhost:8081")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    /**
     * Realiza a autenticação de um usuário e retorna um token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("🔐 Tentativa de login: {}", loginRequest.getEmail());

        try {
            // Autentica o usuário com Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // Define o contexto de segurança
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Carrega o usuário completo
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Recupera a role real no banco
            Optional<User> userOptional = userRepository.findByEmailIgnoreCase(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                logger.warn("❌ Usuário não encontrado no banco.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Usuário não encontrado.", null));
            }

            String role = "ROLE_" + userOptional.get().getRole().toUpperCase();

            // Geração do token JWT
            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), role);

            logger.info("✅ Login realizado com sucesso para: {}", userDetails.getUsername());
            return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", token));

        } catch (Exception e) {
            logger.error("❌ Erro ao autenticar: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("E-mail ou senha inválidos.", null));
        }
    }
}
