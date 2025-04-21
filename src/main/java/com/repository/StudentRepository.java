package com.repository;

import com.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Buscar estudantes com horas pendentes abaixo de um limite
    List<Student> findByHoursPendingLessThanEqual(int hours);

    // Buscar estudante por e-mail (útil para autenticação ou verificação)
    Optional<Student> findByEmail(String email);

    // Verifica duplicidade de CPF
    boolean existsByCpf(String cpf);

    // Verifica duplicidade de e-mail
    boolean existsByEmail(String email);

    // Contagem por status do estágio (Enum)
    @Query("SELECT s.status, COUNT(s) FROM Student s GROUP BY s.status")
    List<Object[]> countByStatus();

    // Contagem por curso
    @Query("SELECT s.course, COUNT(s) FROM Student s GROUP BY s.course")
    List<Object[]> countByCourse();

    // Contagem por nome do professor orientador
    @Query("SELECT s.teacher.name, COUNT(s) FROM Student s GROUP BY s.teacher.name")
    List<Object[]> countByAdvisor();

    /**
     * Retorna estudantes com JOIN FETCH nas entidades relacionadas.
     * Ideal para evitar LazyInitializationException ao acessar college, school e teacher.
     *
     * IMPORTANTE: Evite N+1 e garanta que os relacionamentos estejam presentes ou controlados com `@JsonIgnoreProperties`
     */
    @Query("""
        SELECT s FROM Student s
        LEFT JOIN FETCH s.college
        LEFT JOIN FETCH s.school
        LEFT JOIN FETCH s.teacher
    """)
    List<Student> findAllWithRelations();
}
