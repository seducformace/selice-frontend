package com.service;

import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Retorna todos os usuários
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Cria um novo usuário
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Busca um usuário pelo ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Exclui um usuário pelo ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Autentica um usuário com email e senha
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.filter(u -> u.getPassword().equals(password));
    }
}
