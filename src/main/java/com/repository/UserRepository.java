package com.repository;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável por operações no banco de dados para a entidade User.
 * Estende JpaRepository para acesso rápido a métodos como save, findAll, delete, etc.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo e-mail (login).
     *
     * @param email E-mail do usuário
     * @return Optional com o usuário, se encontrado
     */
    Optional<User> findByEmail(String email);
}
