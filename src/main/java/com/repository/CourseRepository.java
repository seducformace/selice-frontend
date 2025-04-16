package com.repository;

import com.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Buscar cursos por nome contendo (útil para filtros no frontend)
    List<Course> findByNameContainingIgnoreCase(String name);

    // Buscar todos os cursos de uma determinada faculdade
    List<Course> findByFacultyId(Long facultyId);

    // Buscar todos os cursos com coordenador específico
    List<Course> findByCoordinatorId(Long coordinatorId);
}
