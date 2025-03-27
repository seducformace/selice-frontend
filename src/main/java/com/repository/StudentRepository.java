package com.repository;

import com.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByHoursPendingLessThanEqual(int hours);
    Optional<Student> findByEmail(String email);
}
