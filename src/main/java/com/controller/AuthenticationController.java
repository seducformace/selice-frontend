package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.model.User;
import com.model.Student;
import com.repository.UserRepository;
import com.repository.StudentRepository;
import com.service.UserDetailsServiceImpl;
import com.utils.JwtTokenProvider;

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
 * Controlador responsável por autenticar usuários (User ou Student) e gerar o token JWT.
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
    private final StudentRepository studentRepository;

    public AuthenticationController(
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService,
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            StudentRepository studentRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        logger.info("Tentativa de login: {}", email);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 🔍 Primeiro tenta buscar na tabela de usuários
            Optional<User> userOpt = userRepository.findByEmailIgnoreCase(email);
            String role;

            if (userOpt.isPresent()) {
                role = "ROLE_" + userOpt.get().getRole().toUpperCase();
            } else {
                // 🔍 Se não for um User, verifica se é um Student
                Optional<Student> studentOpt = studentRepository.findByEmail(email);
                if (studentOpt.isPresent()) {
                    role = "ROLE_STUDENT";
                } else {
                    logger.warn("Usuário ou aluno não encontrado no banco.");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new LoginResponse("Usuário ou aluno não encontrado.", null));
                }
            }

            // 🔐 Gera token com base na role identificada
            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), role);

            logger.info("Login realizado com sucesso para: {} com papel: {}", email, role);
            return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", token));

        } catch (Exception e) {
            logger.error("Erro ao autenticar: {}", email, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse("E-mail ou senha inválidos.", null));
        }
    }
}
