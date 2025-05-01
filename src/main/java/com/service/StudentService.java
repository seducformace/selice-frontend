package com.service;

import com.dto.StudentDTO;
import com.model.*;
import com.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CoordinatorRepository coordinatorRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public List<StudentDTO> getAllStudentsAsDTO() {
        return studentRepository.findAllWithRelations().stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<StudentDTO> getStudentsByCollegeIdAsDTO(Long collegeId) {
        return studentRepository.findByCollegeIdWithRelations(collegeId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<StudentDTO> getStudentsBySchoolIdAsDTO(Long schoolId) {
        return studentRepository.findBySchoolIdWithRelations(schoolId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<StudentDTO> getStudentsByCoordinatorFaculty(String email) {
        Optional<Coordinator> coordinatorOpt = coordinatorRepository.findByEmailIgnoreCase(email);
        if (coordinatorOpt.isEmpty() || coordinatorOpt.get().getFaculty() == null) {
            return Collections.emptyList();
        }
        Long facultyId = coordinatorOpt.get().getFaculty().getId();
        return studentRepository.findByCollegeIdWithRelations(facultyId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<StudentDTO> getStudentsByCoordinatorSchool(String email) {
        Optional<Coordinator> coordinatorOpt = coordinatorRepository.findByEmailIgnoreCase(email);
        if (coordinatorOpt.isEmpty() || coordinatorOpt.get().getSchool() == null) {
            return Collections.emptyList();
        }
        Long schoolId = coordinatorOpt.get().getSchool().getId();
        return studentRepository.findBySchoolIdWithRelations(schoolId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<StudentDTO> getStudentsByTeacher(String email) {
        return studentRepository.findByTeacherEmailWithRelations(email).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public List<Student> getStudentsByHours(int maxHours) {
        return studentRepository.findByHoursPendingLessThanEqual(maxHours);
    }

    public Student createStudent(Student student) {
        LocalDateTime now = LocalDateTime.now();
        student.setCreatedAt(now);
        student.setUpdatedAt(now);
        student.setRegistrationDate(now);

        student.setHoursCompleted(student.getHoursCompleted() == null ? 0 : student.getHoursCompleted());
        student.setHoursPending(student.getHoursPending() == null ? 0 : student.getHoursPending());
        student.setHoursRemaining(student.getHoursRemaining() == null ? 0 : student.getHoursRemaining());

        userRepository.findByEmailIgnoreCase(student.getEmail()).ifPresentOrElse(
                u -> {},
                () -> {
                    User user = new User();
                    user.setEmail(student.getEmail());
                    user.setName(student.getName());
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole("STUDENT");
                    userRepository.save(user);
                }
        );

        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setName(student.getName());
                    existing.setEmail(student.getEmail());
                    existing.setCpf(student.getCpf());
                    existing.setCourse(student.getCourse()); // Course é uma entidade agora
                    existing.setCollege(student.getCollege());
                    existing.setTeacher(student.getTeacher());
                    existing.setSchool(student.getSchool());
                    existing.setHoursPending(student.getHoursPending() == null ? 0 : student.getHoursPending());
                    existing.setHoursCompleted(student.getHoursCompleted() == null ? 0 : student.getHoursCompleted());
                    existing.setHoursRemaining(student.getHoursRemaining() == null ? 0 : student.getHoursRemaining());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return studentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
    }

    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante com ID " + id + " não encontrado.");
        }
        studentRepository.deleteById(id);
    }

    public Student getAuthenticatedStudent() {
        String email = getAuthenticatedEmail();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado para o e-mail: " + email));
    }

    private String getAuthenticatedEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else if (principal instanceof String stringPrincipal && !stringPrincipal.equalsIgnoreCase("anonymousUser")) {
            return stringPrincipal;
        } else {
            throw new RuntimeException("Usuário não autenticado.");
        }
    }

    private StudentDTO convertToDTO(Student student) {
        String collegeName = "Não vinculado";
        if (student.getCollege() != null && student.getCollege().getName() != null) {
            collegeName = student.getCollege().getName();
        }

        String schoolName = "Não vinculado";
        if (student.getSchool() != null && student.getSchool().getName() != null) {
            schoolName = student.getSchool().getName();
        }

        String teacherName = "Não vinculado";
        if (student.getTeacher() != null && student.getTeacher().getName() != null) {
            teacherName = student.getTeacher().getName();
        }

        String courseName = "Não vinculado";
        if (student.getCourse() != null && student.getCourse().getName() != null) {
            courseName = student.getCourse().getName();
        }

        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getCpf(),
                student.getEmail(),
                courseName,
                collegeName,
                schoolName,
                teacherName
        );
    }
}
