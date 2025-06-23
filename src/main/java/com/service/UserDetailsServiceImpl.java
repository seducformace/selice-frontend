package com.service;

import com.model.User;
import com.model.Student;
import com.repository.UserRepository;
import com.repository.StudentRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Implementação personalizada de UserDetailsService
 * que carrega os detalhes do usuário (User ou Student) para autenticação.
 */
@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public UserDetailsServiceImpl(UserRepository userRepository, StudentRepository studentRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * Tenta carregar um usuário autenticável (User ou Student) com base no e-mail.
     * Se encontrado, devolve um objeto UserDetails com as devidas permissões.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("🔍 Tentando autenticar com email: {}", email);

        return userRepository.findByEmailIgnoreCase(email)
                .map(user -> {
                    logger.info("✅ Usuário encontrado: {}", user.getEmail());
                    String role = "ROLE_" + user.getRole().toUpperCase(); // Sempre prefixar ROLE_

                    // 🔐 DEBUG: Verificação manual de senha "123" contra o hash armazenado
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    boolean passwordMatches = encoder.matches("123", user.getPassword());
                    logger.debug("🔐 [DEBUG MANUAL] Senha fornecida (123) confere com hash do banco? {}", passwordMatches);
                    logger.debug("🔐 Hash armazenado no banco: {}", user.getPassword());

                    return org.springframework.security.core.userdetails.User
                            .withUsername(user.getEmail())
                            .password(user.getPassword())
                            .authorities(Collections.singleton(new SimpleGrantedAuthority(role)))
                            .build();
                })
                .orElseGet(() -> studentRepository.findByEmail(email)
                        .map(student -> {
                            logger.info("✅ Aluno encontrado: {}", student.getEmail());
                            return org.springframework.security.core.userdetails.User
                                    .withUsername(student.getEmail())
                                    .password(student.getPassword())
                                    .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")))
                                    .build();
                        })
                        .orElseThrow(() -> {
                            logger.warn("⛔ Nenhum usuário ou aluno encontrado com o email: {}", email);
                            return new UsernameNotFoundException("Usuário ou aluno não encontrado com o email: " + email);
                        }));
    }
}
