package com.repository;

import com.model.Coordinator;
import com.projection.CoordinatorCourseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

    // Verifica se já existe coordenador com o e-mail informado
    boolean existsByEmail(String email);

    // Verifica se já existe coordenador com o CPF informado
    boolean existsByCpf(String cpf);

    // Coordenadores agrupados por curso
    @Query("SELECT c.department AS course, COUNT(c) AS total FROM Coordinator c GROUP BY c.department")
    List<CoordinatorCourseCount> getCoordinatorCountsByCourse();

    // TODO: Se adicionar um campo de lista de cursos futuramente, reativar a consulta abaixo.
    // long countByCoursesIsNull();
}
