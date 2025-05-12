package com.service;

import com.dto.StudentDTO;
import com.model.Coordinator;
import com.model.Student;
import com.repository.CoordinatorRepository;
import com.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    public List<StudentDTO> getAllStudentsAsDTO() {
        String role = getAuthenticatedRole();
        String email = getAuthenticatedEmail();

        System.out.println("🔐 Role detectada: " + role);
        System.out.println("📧 Email detectado: " + email);

        if ("ROLE_ADMIN".equals(role) || "ADMIN".equals(role)) {
            return studentRepository.findAllWithRelations().stream()
                    .map(this::convertToDTO)
                    .toList();
        }

        if ("ROLE_TEACHER".equals(role) || "TEACHER".equals(role)) {
            return studentRepository.findByTeacherEmailWithRelations(email).stream()
                    .map(this::convertToDTO)
                    .toList();
        }

        if ("ROLE_COORDINATOR_FACULTY".equals(role) || "COORDINATOR_FACULTY".equals(role)) {
            return coordinatorRepository.findByEmailIgnoreCase(email)
                    .filter(c -> c.getFaculty() != null)
                    .map(c -> studentRepository.findByCollegeIdWithRelations(c.getFaculty().getId()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }

        if ("ROLE_COORDINATOR_SCHOOL".equals(role) || "COORDINATOR_SCHOOL".equals(role)) {
            return coordinatorRepository.findByEmailIgnoreCase(email)
                    .filter(c -> c.getSchool() != null)
                    .map(c -> studentRepository.findBySchoolIdWithRelations(c.getSchool().getId()))
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }

        if ("ROLE_STUDENT".equals(role) || "STUDENT".equals(role)) {
            return List.of(convertToDTO(getAuthenticatedStudent()));
        }

        return Collections.emptyList();
    }

    public Page<StudentDTO> getAllStudentsPaginated(Pageable pageable, String name) {
        String role = getAuthenticatedRole();
        String email = getAuthenticatedEmail();

        System.out.println("🔍 [Paginação] Role: " + role);
        System.out.println("🔍 [Paginação] Email: " + email);
        System.out.println("🔍 [Paginação] Filtro de nome: " + name);

        Page<Student> page;

        if (name != null && !name.isBlank()) {
            if ("ROLE_ADMIN".equals(role)) {
                page = studentRepository.findByNameContainingIgnoreCase(name, pageable);
            } else if ("ROLE_COORDINATOR_FACULTY".equals(role)) {
                Optional<Coordinator> coordinator = coordinatorRepository.findByEmailIgnoreCase(email);
                if (coordinator.isEmpty()) {
                    System.out.println("❌ Coordenador de faculdade não encontrado pelo e-mail.");
                    return Page.empty();
                }
                if (coordinator.get().getFaculty() == null) {
                    System.out.println("⚠️ Coordenador não possui faculdade vinculada.");
                    return Page.empty();
                }
                Long facultyId = coordinator.get().getFaculty().getId();
                System.out.println("✅ Coordenador de faculdade encontrado. ID da faculdade: " + facultyId);
                page = studentRepository.findByCollegeIdAndNameContainingIgnoreCase(facultyId, name, pageable);
            } else if ("ROLE_COORDINATOR_SCHOOL".equals(role)) {
                Optional<Coordinator> coordinator = coordinatorRepository.findByEmailIgnoreCase(email);
                if (coordinator.isEmpty()) {
                    System.out.println("❌ Coordenador escolar não encontrado pelo e-mail.");
                    return Page.empty();
                }
                if (coordinator.get().getSchool() == null) {
                    System.out.println("⚠️ Coordenador não possui escola vinculada.");
                    return Page.empty();
                }
                Long schoolId = coordinator.get().getSchool().getId();
                System.out.println("✅ Coordenador escolar encontrado. ID da escola: " + schoolId);
                page = studentRepository.findBySchoolIdAndNameContainingIgnoreCase(schoolId, name, pageable);
            } else {
                return Page.empty();
            }
        } else {
            if ("ROLE_ADMIN".equals(role)) {
                page = studentRepository.findAll(pageable);
            } else if ("ROLE_COORDINATOR_FACULTY".equals(role)) {
                Optional<Coordinator> coordinator = coordinatorRepository.findByEmailIgnoreCase(email);
                if (coordinator.isEmpty()) {
                    System.out.println("❌ Coordenador de faculdade não encontrado.");
                    return Page.empty();
                }
                if (coordinator.get().getFaculty() == null) {
                    System.out.println("⚠️ Coordenador não possui faculdade vinculada.");
                    return Page.empty();
                }
                Long facultyId = coordinator.get().getFaculty().getId();
                System.out.println("✅ Coordenador de faculdade encontrado. ID da faculdade: " + facultyId);
                page = studentRepository.findByCollegeId(facultyId, pageable);
            } else if ("ROLE_COORDINATOR_SCHOOL".equals(role)) {
                Optional<Coordinator> coordinator = coordinatorRepository.findByEmailIgnoreCase(email);
                if (coordinator.isEmpty()) {
                    System.out.println("❌ Coordenador escolar não encontrado.");
                    return Page.empty();
                }
                if (coordinator.get().getSchool() == null) {
                    System.out.println("⚠️ Coordenador não possui escola vinculada.");
                    return Page.empty();
                }
                Long schoolId = coordinator.get().getSchool().getId();
                System.out.println("✅ Coordenador escolar encontrado. ID da escola: " + schoolId);
                page = studentRepository.findBySchoolId(schoolId, pageable);
            } else {
                return Page.empty();
            }
        }

        return page.map(this::convertToDTO);
    }

    public StudentDTO convertToDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getName(),
                student.getCpf(),
                student.getEmail(),
                student.getCourse() != null ? student.getCourse().getName() : "Não vinculado",
                student.getCollege() != null ? student.getCollege().getName() : "Não vinculado",
                student.getSchool() != null ? student.getSchool().getName() : "Não vinculado",
                student.getTeacher() != null ? student.getTeacher().getName() : "Não vinculado",
                student.getCollege() != null ? student.getCollege().getId() : null,
                student.getSchool() != null ? student.getSchool().getId() : null,
                student.getTeacher() != null ? student.getTeacher().getId() : null,
                student.getCourse() != null ? student.getCourse().getId() : null
        );
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student getAuthenticatedStudent() {
        String email = getAuthenticatedEmail();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Estudante autenticado não encontrado."));
    }

    public List<Student> getStudentsByHours(int maxHours) {
        return studentRepository.findByHoursPendingLessThanEqual(maxHours);
    }

    @Transactional
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante não encontrado com ID: " + id);
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante não encontrado com ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public void assignStudentToSchool(Long studentId, Long schoolId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + studentId));

        student.getSchool().setId(schoolId);
        studentRepository.save(student);
    }

    private String getAuthenticatedEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        return principal.toString();
    }

    private String getAuthenticatedRole() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(auth -> auth.getAuthority())
                    .orElse("UNKNOWN");
        }
        return "UNKNOWN";
    }
}
