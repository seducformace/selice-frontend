package com.repository;

import com.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para a entidade Coordinator.
 * Aqui é onde o sistema conversa diretamente com o banco de dados.
 * Fornece métodos prontos e eficientes para operações básicas e personalizadas!
 */
@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

    /**
     * Método personalizado para buscar um coordenador pelo e-mail.
     * Retorna um Optional que pode conter o coordenador encontrado ou estar vazio,
     * caso não exista um coordenador com o e-mail fornecido.
     *
     * @param email O e-mail do coordenador.
     * @return Optional contendo o coordenador, se encontrado.
     */
    Optional<Coordinator> findByEmail(String email);
}
