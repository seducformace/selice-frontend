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

/**
 * Repositório responsável pelas operações com a entidade Student.
 * Suporte a autenticação, filtros e carregamento completo de relacionamentos.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 🔍 Busca individual por e-mail (utilizado na autenticação de alunos)
    Optional<Student> findByEmail(String email);

    // ⏳ Busca por estudantes com carga horária pendente inferior ou igual ao valor informado
    List<Student> findByHoursPendingLessThanEqual(int maxHours);

    // 📄 Paginação com filtro por nome (acesso geral/admin)
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // 📄 Paginação com filtro por nome, restrita a uma faculdade
    Page<Student> findByCollegeIdAndNameContainingIgnoreCase(Long collegeId, String name, Pageable pageable);

    // 📄 Paginação com filtro por nome, restrita a uma escola
    Page<Student> findBySchoolIdAndNameContainingIgnoreCase(Long schoolId, String name, Pageable pageable);

    // 📄 Paginação completa por faculdade (sem filtro por nome)
    Page<Student> findByCollegeId(Long collegeId, Pageable pageable);

    // 📄 Paginação completa por escola (sem filtro por nome)
    Page<Student> findBySchoolId(Long schoolId, Pageable pageable);

    // 📚 Busca completa (admin) com todos os relacionamentos carregados (college, school, course, teacher)
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
    """)
    List<Student> findAllWithRelations();

    // 🎓 Busca completa por faculdade (com relacionamentos)
    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
        WHERE c.id = :collegeId
    """)
    List<Student> findByCollegeIdWithRelations(@Param("collegeId") Long collegeId);

    // 🏫 Busca completa por escola (com relacionamentos)
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
        WHERE s.school.id = :schoolId
    """)
    List<Student> findBySchoolIdWithRelations(@Param("schoolId") Long schoolId);

    // 👨‍🏫 Busca por professor associado (usado no dashboard do professor)
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
        LEFT JOIN FETCH s.course
        WHERE s.teacher.email = :email
    """)
    List<Student> findByTeacherEmailWithRelations(@Param("email") String email);

    // 🔒 Busca um estudante por e-mail, com todos os relacionamentos carregados (usado no login)
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
