package com.repository;

import com.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Verifica se já existe professor com o e-mail
    boolean existsByEmail(String email);

    // Consulta professores vinculados a uma escola específica
    @Query("SELECT t FROM Teacher t JOIN t.schools s WHERE s.id = :schoolId")
    List<Teacher> findBySchoolId(Long schoolId);
}
