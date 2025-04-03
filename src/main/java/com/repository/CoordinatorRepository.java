package com.repository;

import com.model.Coordinator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável por realizar operações de persistência
 * para a entidade Coordinator no banco de dados.
 *
 * Estende JpaRepository, que fornece métodos prontos como:
 * save, findById, findAll, deleteById, entre outros.
 */
@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

    /**
     * Busca um coordenador pelo e-mail.
     * Retorna um Optional, que evita problemas com valores nulos.
     *
     * @param email E-mail do coordenador a ser buscado.
     * @return Optional contendo o coordenador, se encontrado.
     */
    Optional<Coordinator> findByEmail(String email);
}
