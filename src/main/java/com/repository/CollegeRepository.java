package com.repository;

import com.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório responsável por gerenciar as operações de persistência relacionadas às faculdades.
 * Aqui podemos armazenar, buscar e manter as faculdades organizadas no sistema.
 */
@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {

    /**
     * Busca uma faculdade pelo nome utilizando uma consulta JPQL.
     *
     * @param name Nome exato da faculdade.
     * @return Instância da faculdade correspondente ao nome fornecido, ou um Optional vazio se não encontrada.
     */
    @Query("SELECT c FROM College c WHERE c.name = :name")
    Optional<College> findByName(@Param("name") String name);
}
