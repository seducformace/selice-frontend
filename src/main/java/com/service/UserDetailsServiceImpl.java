package com.service;

import com.model.User;
import com.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação personalizada de UserDetailsService
 * que carrega os detalhes do usuário para autenticação.
 */
@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Carrega usuário pelo email fornecido durante o login.
     * @param email E-mail fornecido para autenticação.
     * @return UserDetails com dados do usuário encontrados no banco.
     * @throws UsernameNotFoundException caso o usuário não exista.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Tentando carregar usuário por email: {}", email);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> {
                    logger.warn("Usuário não encontrado: {}", email);
                    return new UsernameNotFoundException("Usuário não encontrado: " + email);
                });

        logger.info("Usuário encontrado: {}", user.getEmail());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole()) // ✅ Corrigido: removido .name()
                .build();
    }
}
