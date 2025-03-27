package com.service;

import com.model.User;
import com.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Tentando autenticar usuário com email: {}", email);

        // Busca o usuário pelo email no banco de dados
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Usuário não encontrado com o email: {}", email);
                    return new UsernameNotFoundException("Usuário não encontrado com o email: " + email);
                });

        logger.info("Usuário encontrado: {}", user.getEmail());
        logger.info("Roles do usuário: {}", user.getRole());

        // Cria e retorna um UserDetails com os dados do usuário
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Define o email como nome de usuário
                .password(user.getPassword()) // Senha armazenada no banco (hashada)
                .roles(processRole(user.getRole())) // Processa a role para garantir compatibilidade
                .build();
    }

    /**
     * Processa a role para garantir que está no formato esperado pelo Spring Security.
     *
     * @param role A role do usuário no banco de dados.
     * @return A role ajustada (sem duplicação do prefixo ROLE_).
     */
    private String processRole(String role) {
        if (role.startsWith("ROLE_")) {
            return role.substring(5); // Remove o prefixo "ROLE_" se já existir
        }
        return role;
    }
}
