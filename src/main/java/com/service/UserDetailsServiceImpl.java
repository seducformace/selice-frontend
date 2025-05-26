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

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("🟡 Tentando autenticar com email: {}", email);

        // 🔍 Tenta encontrar como User
        User user = userRepository.findByEmailIgnoreCase(email).orElse(null);
        if (user != null) {
            logger.info("✅ Usuário encontrado: {}", user.getEmail());
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPassword())
                    .authorities(user.getRole().toUpperCase())
                    .build();
        }

        // 🔍 Tenta encontrar como Student
        logger.info("🟡 Usuário não encontrado, tentando autenticar como aluno...");
        Student student = studentRepository.findByEmail(email).orElse(null);
        if (student != null) {
            logger.info("✅ Aluno encontrado: {}", student.getEmail());
            return org.springframework.security.core.userdetails.User
                    .withUsername(student.getEmail())
                    .password(student.getPassword())
                    .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_STUDENT")))
                    .build();
        }

        // ❌ Nenhum encontrado
        logger.warn("⛔ Nenhum usuário ou aluno encontrado com o email: {}", email);
        throw new UsernameNotFoundException("Usuário ou aluno não encontrado com o email: " + email);
    }
}
