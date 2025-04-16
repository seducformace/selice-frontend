package com.repository;

import com.model.Coordinator;
import com.projection.CoordinatorCourseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

    // Coordenadores agrupados por curso
    @Query("SELECT c.department AS course, COUNT(c) AS total FROM Coordinator c GROUP BY c.department")
    List<CoordinatorCourseCount> getCoordinatorCountsByCourse();

    // TODO: Se existir um campo representando relacionamento com curso, como 'List<Course> courses',
    // então reative ou corrija este método.
    // Exemplo: long countByCursosIsNull();
    // long countByCoursesIsNull(); <-- REMOVIDO pois o campo 'courses' não existe na entidade Coordinator
}
