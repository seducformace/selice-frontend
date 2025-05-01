package com.repository;

import com.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByHoursPendingLessThanEqual(int hours);

    Optional<Student> findByEmail(String email);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    @Query("SELECT s.status, COUNT(s) FROM Student s GROUP BY s.status")
    List<Object[]> countByStatus();

    @Query("SELECT s.course.name, COUNT(s) FROM Student s GROUP BY s.course.name")
    List<Object[]> countByCourse(); // 🔄 Agora usa o nome da entidade Course

    @Query("SELECT s.teacher.name, COUNT(s) FROM Student s GROUP BY s.teacher.name")
    List<Object[]> countByAdvisor();

    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school sc
        LEFT JOIN FETCH s.teacher t
        LEFT JOIN FETCH s.course cs
    """)
    List<Student> findAllWithRelations(); // 🔄 Adicionado JOIN com course

    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school sc
        LEFT JOIN FETCH s.teacher t
        LEFT JOIN FETCH s.course cs
        WHERE s.college.id = :collegeId
    """)
    List<Student> findByCollegeIdWithRelations(Long collegeId);

    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school sc
        LEFT JOIN FETCH s.teacher t
        LEFT JOIN FETCH s.course cs
        WHERE s.school.id = :schoolId
    """)
    List<Student> findBySchoolIdWithRelations(Long schoolId);

    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school sc
        LEFT JOIN FETCH s.teacher t
        LEFT JOIN FETCH s.course cs
        WHERE s.college.id IN (
            SELECT c.faculty.id FROM Coordinator c WHERE UPPER(c.email) = UPPER(:email)
        )
    """)
    List<Student> findByCoordinatorFacultyEmail(String email);

    @Query("""
        SELECT DISTINCT s FROM Student s
        LEFT JOIN FETCH s.college c
        LEFT JOIN FETCH s.school sc
        LEFT JOIN FETCH s.teacher t
        LEFT JOIN FETCH s.course cs
        WHERE UPPER(s.teacher.email) = UPPER(:email)
    """)
    List<Student> findByTeacherEmailWithRelations(String email);
}
