package com.service;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.model.User;
import com.repository.UserRepository;
import com.utils.JwtTokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        // 🔐 Autentica com email e senha
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // ✅ Define contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 🔍 Busca o usuário autenticado no banco
        User user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 🔑 Gera o token JWT com email e role do usuário
        String token = jwtTokenProvider.generateToken(user.getEmail(), "ROLE_" + user.getRole().toUpperCase());

        // ✅ Retorna resposta com token
        return new LoginResponse("Autenticação realizada com sucesso!", token);
    }
}
