package com.repository;

import com.model.School;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para realizar operações de persistência relacionadas às escolas.
 */
@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    /**
     * Busca uma escola pelo nome utilizando uma consulta JPQL.
     *
     * @param name Nome exato da escola.
     * @return Optional contendo a instância da escola correspondente ao nome fornecido, ou vazio se não encontrada.
     *
     * Exemplo de uso:
     * findByName("Escola Estadual ABC");
     */
    @Query("SELECT s FROM School s WHERE s.name = :name")
    Optional<School> findByName(@Param("name") String name);

    /**
     * Busca todas as escolas com um nome que contenha uma determinada palavra-chave (consulta case insensitive).
     *
     * @param keyword Palavra-chave a ser buscada no nome da escola.
     * @return Lista de escolas que correspondem à palavra-chave.
     *
     * Exemplo de uso:
     * findByNameContainingIgnoreCase("Estadual");
     */
    @Query("SELECT s FROM School s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<School> findByNameContainingIgnoreCase(@Param("keyword") String keyword);

    /**
     * Busca todas as escolas com paginação.
     *
     * @param pageable Objeto de paginação e ordenação.
     * @return Página contendo escolas.
     *
     * Exemplo de uso:
     * findAll(PageRequest.of(0, 10, Sort.by("name").ascending()));
     */
    Page<School> findAll(Pageable pageable);
}
