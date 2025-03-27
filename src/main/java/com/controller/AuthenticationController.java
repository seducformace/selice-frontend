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
import org.springframework.security.authentication.BadCredentialsException;
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
@CrossOrigin(origins = "http://localhost:8081") // Libera frontend local
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserDetailsServiceImpl userDetailsService,
                                    JwtTokenProvider jwtTokenProvider,
                                    UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint POST para autenticação de usuários.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Tentativa de login para: {}", loginRequest.getEmail());

        try {
            // Autentica usuário (email + senha)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Carrega os detalhes (padrão Spring Security)
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            // Recupera usuário do banco (para extrair a role real)
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                logger.warn("Usuário não encontrado no banco de dados.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginResponse("Usuário não encontrado no banco.", null));
            }

            String role = "ROLE_" + userOptional.get().getRole().toUpperCase(); // Ex: ADMIN → ROLE_ADMIN
            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), role);

            logger.info("Login realizado com sucesso: {}", userDetails.getUsername());

            return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", token));

        } catch (BadCredentialsException e) {
            logger.warn("Credenciais inválidas para: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("Email ou senha incorretos.", null));

        } catch (Exception e) {
            logger.error("Erro inesperado durante a autenticação para: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse("Erro interno no servidor. Verifique os logs.", null));
        }
    }
}
