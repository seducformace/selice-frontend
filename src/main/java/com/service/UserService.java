package com.service;

import com.model.User;
import com.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço de usuários com criptografia segura para senhas.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // ✅ Injeção do codificador

    /**
     * Retorna todos os usuários cadastrados.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Cria um novo usuário com a senha criptografada.
     */
    public User createUser(User user) {
        // ✅ Codifica a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Busca um usuário pelo ID.
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Exclui um usuário pelo ID.
     */
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // ❌ Este método foi removido. A autenticação é feita com segurança no AuthenticationController + Spring Security.
}
