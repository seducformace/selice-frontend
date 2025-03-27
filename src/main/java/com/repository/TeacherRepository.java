package com.repository;

import com.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para gerenciar operações com professores.
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * Busca um professor pelo nome.
     *
     * @param name Nome do professor.
     * @return Um Optional contendo o professor, se encontrado.
     */
    @Query("SELECT t FROM Teacher t WHERE t.name = :name")
    Optional<Teacher> findByName(@Param("name") String name);

    /**
     * Busca um professor pelo email.
     *
     * @param email Email do professor.
     * @return Um Optional contendo o professor, se encontrado.
     */
    @Query("SELECT t FROM Teacher t WHERE t.email = :email")
    Optional<Teacher> findByEmail(@Param("email") String email);
}
