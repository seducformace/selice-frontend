package com.repository;

import com.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório responsável pela persistência e consulta
 * da entidade Faculty no banco de dados.
 *
 * Estende JpaRepository, o que fornece acesso automático
 * a diversos métodos CRUD e de paginação.
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    /**
     * Aqui podemos declarar métodos personalizados,
     * usando a convenção de nomes do Spring Data JPA.
     *
     * Exemplos (não implementados ainda):
     */

    // Buscar uma faculdade pelo código MEC
    // Optional<Faculty> findByMecCode(String mecCode);

    // Buscar todas as faculdades de uma cidade específica
    // List<Faculty> findByCity(String city);

    // Contar quantas faculdades existem em um estado
    // long countByState(String state);
}
