package com.repository;

import com.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 🔍 Busca individual por e-mail
    Optional<Student> findByEmail(String email);

    // ⏳ Busca por carga horária pendente
    List<Student> findByHoursPendingLessThanEqual(int maxHours);

    // 📄 Paginação com filtro de nome (Admin)
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 📄 Paginação por faculdade com filtro de nome
    Page<Student> findByCollegeIdAndNameContainingIgnoreCase(Long collegeId, String name, Pageable pageable);

    // 📄 Paginação por escola com filtro de nome
    Page<Student> findBySchoolIdAndNameContainingIgnoreCase(Long schoolId, String name, Pageable pageable);

    // 📄 Paginação por faculdade (sem filtro)
    Page<Student> findByCollegeId(Long collegeId, Pageable pageable);

    // 📄 Paginação por escola (sem filtro)
    Page<Student> findBySchoolId(Long schoolId, Pageable pageable);

    // 📚 Busca completa (Admin) com todos os relacionamentos carregados
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
    """)
    List<Student> findAllWithRelations();

    // 🎓 Busca completa por faculdade com todos os relacionamentos (usado por coordenador de faculdade)
    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
        WHERE c.id = :collegeId
    """)
    List<Student> findByCollegeIdWithRelations(@Param("collegeId") Long collegeId);

    // 🏫 Busca completa por escola com todos os relacionamentos (usado por coordenador escolar)
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
        WHERE s.school.id = :schoolId
    """)
    List<Student> findBySchoolIdWithRelations(@Param("schoolId") Long schoolId);

    // 👨‍🏫 Busca por professor com todos os relacionamentos (usado por professores)
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
        WHERE s.teacher.email = :email
    """)
    List<Student> findByTeacherEmailWithRelations(@Param("email") String email);
    // 🎓 Busca um estudante autenticado com todos os relacionamentos
    @Query("""
    SELECT s FROM Student s
    LEFT JOIN FETCH s.college
    LEFT JOIN FETCH s.school
    LEFT JOIN FETCH s.teacher
    LEFT JOIN FETCH s.course
    WHERE LOWER(s.email) = LOWER(:email)
""")
    Optional<Student> findStudentWithRelationsByEmail(@Param("email") String email);

}
