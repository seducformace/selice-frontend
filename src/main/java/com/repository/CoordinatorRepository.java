package com.repository;

import com.model.Coordinator;
import com.projection.CoordinatorCourseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordinatorRepository extends JpaRepository<Coordinator, Long> {

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Optional<Coordinator> findByEmailIgnoreCase(String email);

    @Query("SELECT c.department AS course, COUNT(c) AS total FROM Coordinator c GROUP BY c.department")
    List<CoordinatorCourseCount> getCoordinatorCountsByCourse();

    @Query("SELECT c FROM Coordinator c LEFT JOIN FETCH c.linkedFaculties WHERE LOWER(c.email) = LOWER(:email)")
    Optional<Coordinator> findWithLinkedFaculties(String email);

    // ✅ Novo método para carregar a escola junto com o coordenador
    @Query("SELECT c FROM Coordinator c LEFT JOIN FETCH c.school WHERE LOWER(c.email) = LOWER(:email)")
    Optional<Coordinator> findByEmailWithSchool(String email);
}
