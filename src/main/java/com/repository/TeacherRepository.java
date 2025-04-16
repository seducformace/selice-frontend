package com.repository;

import com.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Verifica se já existe professor com o e-mail informado
    boolean existsByEmail(String email);

    // Verifica se já existe professor com o CPF informado
    boolean existsByCpf(String cpf);

    // Consulta professores vinculados a uma escola específica
    @Query("SELECT t FROM Teacher t JOIN t.schools s WHERE s.id = :schoolId")
    List<Teacher> findBySchoolId(Long schoolId);

    // Quantidade de professores por escola (para relatórios de gestão escolar)
    @Query("SELECT COUNT(t) FROM Teacher t JOIN t.schools s WHERE s.id = :schoolId")
    long countBySchoolId(Long schoolId);

    // ✅ Professores agrupados por disciplina (corrigido para campo real da entidade)
    @Query("SELECT t.discipline AS course, COUNT(t) AS total FROM Teacher t GROUP BY t.discipline")
    List<Object[]> countTeachersByCourse();
}
