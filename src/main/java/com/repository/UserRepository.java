package com.repository;

import com.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável por interações com a tabela "users" no banco de dados.
 * Permite operações básicas como salvar, buscar, atualizar e excluir usuários.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo e-mail (case insensitive).
     *
     * @param email E-mail do usuário
     * @return Optional com o usuário encontrado, ou vazio se não existir
     */
    Optional<User> findByEmailIgnoreCase(String email);
}
