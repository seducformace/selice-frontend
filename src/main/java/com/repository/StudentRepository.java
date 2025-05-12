package com.repository;

import com.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    List<Student> findByHoursPendingLessThanEqual(int maxHours);

    // Paginação com filtro por nome (Admin)
    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Paginação por faculdade com filtro por nome
    Page<Student> findByCollegeIdAndNameContainingIgnoreCase(Long collegeId, String name, Pageable pageable);

    // Paginação por escola com filtro por nome
    Page<Student> findBySchoolIdAndNameContainingIgnoreCase(Long schoolId, String name, Pageable pageable);

    // Paginação por faculdade (sem filtro por nome)
    Page<Student> findByCollegeId(Long collegeId, Pageable pageable);

    // Paginação por escola (sem filtro por nome)
    Page<Student> findBySchoolId(Long schoolId, Pageable pageable);

    // Busca todos com joins (admin)
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.college LEFT JOIN FETCH s.school LEFT JOIN FETCH s.teacher LEFT JOIN FETCH s.course")
    List<Student> findAllWithRelations();

    // Busca por faculdade com joins
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.college LEFT JOIN FETCH s.school LEFT JOIN FETCH s.teacher LEFT JOIN FETCH s.course WHERE s.college.id = :collegeId")
    List<Student> findByCollegeIdWithRelations(Long collegeId);

    // Busca por escola com joins
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.college LEFT JOIN FETCH s.school LEFT JOIN FETCH s.teacher LEFT JOIN FETCH s.course WHERE s.school.id = :schoolId")
    List<Student> findBySchoolIdWithRelations(Long schoolId);

    // Busca por professor com joins
    @Query("SELECT s FROM Student s LEFT JOIN FETCH s.college LEFT JOIN FETCH s.school LEFT JOIN FETCH s.teacher LEFT JOIN FETCH s.course WHERE s.teacher.email = :email")
    List<Student> findByTeacherEmailWithRelations(String email);
}
