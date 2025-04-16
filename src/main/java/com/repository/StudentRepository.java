package com.repository;

import com.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Retorna alunos com horas pendentes abaixo ou igual ao valor especificado
    List<Student> findByHoursPendingLessThanEqual(int hours);

    // Busca aluno por e-mail
    Optional<Student> findByEmail(String email);

    // Verifica se já existe aluno com o mesmo CPF
    boolean existsByCpf(String cpf);

    // Verifica se já existe aluno com o mesmo e-mail
    boolean existsByEmail(String email);

    // Contagem de estagiários por status (para relatórios)
    @Query("SELECT s.status, COUNT(s) FROM Student s GROUP BY s.status")
    List<Object[]> countByStatus();

    // Contagem de estagiários por curso
    @Query("SELECT s.course, COUNT(s) FROM Student s GROUP BY s.course")
    List<Object[]> countByCourse();

    // Contagem por professor orientador
    @Query("SELECT s.teacher.name, COUNT(s) FROM Student s GROUP BY s.teacher.name")
    List<Object[]> countByAdvisor();
}
