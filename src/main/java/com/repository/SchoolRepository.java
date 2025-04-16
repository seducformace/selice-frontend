package com.repository;

import com.model.School;
import com.dto.SchoolDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {

    long countByType(String type);

    List<School> findByNameContainingIgnoreCase(String name);

    /**
     * Retorna estatísticas detalhadas das escolas para os relatórios.
     * Inclui coordenador, total de professores, estagiários e alunos por escola.
     */
    @Query("SELECT new com.dto.SchoolDetails(" +
            "s.name, " +
            "(SELECT COALESCE(MAX(c.name), 'Não vinculado') FROM Coordinator c WHERE c.school.id = s.id), " +
            "(SELECT COUNT(t) FROM Teacher t JOIN t.schools sch WHERE sch.id = s.id), " +
            "(SELECT COUNT(st) FROM Student st WHERE st.school.id = s.id AND st.status = com.enums.InternshipStatus.EM_ANDAMENTO), " +
            "(SELECT COUNT(st) FROM Student st WHERE st.school.id = s.id AND st.status = com.enums.InternshipStatus.CONCLUIDO), " +
            "(SELECT COUNT(st) FROM Student st WHERE st.school.id = s.id AND st.status = com.enums.InternshipStatus.REPROVADO), " +
            "(SELECT COUNT(st) FROM Student st WHERE st.school.id = s.id)" +
            ") FROM School s")
    List<SchoolDetails> getSchoolsDetailedStats();

}
