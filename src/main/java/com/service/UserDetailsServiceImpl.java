package com.service;

import com.model.User;
import com.repository.UserRepository;

import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Serviço responsável por carregar usuários do banco
 * para o processo de autenticação do Spring Security.
 */
@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Injeta o repositório de usuários
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Método chamado automaticamente pelo Spring Security
     * ao autenticar um usuário.
     *
     * @param email E-mail do usuário (username)
     * @return Detalhes do usuário (UserDetails)
     * @throws UsernameNotFoundException Se o usuário não existir
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        // Constrói um usuário do Spring Security com email, senha e roles
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getEmail());
        builder.password(user.getPassword());

        // Adiciona a role única (vinda do campo `user.getRole()`)
        builder.roles(user.getRole()); // ⚠️ Certifique-se que está sem prefixo "ROLE_" no banco

        return builder.build();
    }
}
