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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // ✅ Correção feita aqui: injeção correta

    public List<StudentDTO> getAllStudentsAsDTO() {
        System.out.println("🔍 [DEBUG] Entrou no método de busca de estudantes");
        String role = getAuthenticatedRole();
        String email = getAuthenticatedEmail();
        System.out.println("📛 Role recebido: " + role);
        System.out.println("📧 Email recebido: " + email);

        if (isAdmin(role)) {
            return studentRepository.findAllWithRelations()
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }

        if (isFacultyCoordinator(role)) {
            return coordinatorRepository.findWithLinkedFaculties(email)
                    .map(coordinator -> {
                        var faculties = coordinator.getLinkedFaculties();
                        if (faculties == null || faculties.isEmpty()) {
                            System.out.println("⚠️ Coordenador de faculdade não possui faculdades vinculadas.");
                            return Collections.<StudentDTO>emptyList();
                        }

                        return faculties.stream()
                                .flatMap(faculty -> {
                                    var students = studentRepository.findByCollegeIdWithRelations(faculty.getId());
                                    System.out.printf("📚 Faculdade ID %d → %d aluno(s)%n", faculty.getId(), students.size());
                                    return students.stream();
                                })
                                .map(this::convertToDTO)
                                .toList();
                    })
                    .orElse(Collections.emptyList());
        }

        if (isSchoolCoordinator(role)) {
            return coordinatorRepository.findByEmailWithSchool(email)
                    .filter(c -> c.getSchool() != null)
                    .map(c -> {
                        Long schoolId = c.getSchool().getId();
                        var students = studentRepository.findBySchoolIdWithRelations(schoolId);
                        System.out.println("✅ Chamando studentRepository.findBySchoolIdWithRelations com ID " + schoolId);
                        students = studentRepository.findBySchoolIdWithRelations(schoolId);
                        System.out.println("✅ Estudantes retornados: " + students.size());
                        System.out.printf("🏫 Escola ID %d retornou %d aluno(s)%n", schoolId, students.size());
                        return students.stream()
                                .map(this::convertToDTO)
                                .toList();
                    })
                    .orElse(Collections.emptyList());
        }

        if (isTeacher(role)) {
            var students = studentRepository.findByTeacherEmailWithRelations(email);
            System.out.printf("👨‍🏫 Professor retornou %d aluno(s)%n", students.size());
            return students.stream().map(this::convertToDTO).toList();
        }

        System.out.println("❌ Nenhum perfil reconhecido.");
        return Collections.emptyList();
    }

    public Page<StudentDTO> getAllStudentsPaginated(Pageable pageable, String name) {
        String role = getAuthenticatedRole();
        String email = getAuthenticatedEmail();
        System.out.println("🔍 Role recebido: '" + role + "'");
        System.out.println("✅ isSchoolCoordinator(role)? " + isSchoolCoordinator(role));
        System.out.println("🔍 [DEBUG] Entrou no método de busca de estudantes paginada");
        System.out.println("📛 Role recebido: " + role);
        System.out.println("📧 Email recebido: " + email);

        if (isAdmin(role)) {
            if (name != null && !name.isBlank()) {
                return studentRepository.findByNameContainingIgnoreCase(name, pageable).map(this::convertToDTO);
            } else {
                return studentRepository.findAll(pageable).map(this::convertToDTO);
            }
        }

        if (isFacultyCoordinator(role)) {
            Optional<Coordinator> coordinatorOpt = coordinatorRepository.findWithLinkedFaculties(email);
            if (coordinatorOpt.isEmpty()) return Page.empty();

            Coordinator coordinator = coordinatorOpt.get();
            if (coordinator.getLinkedFaculties() == null || coordinator.getLinkedFaculties().isEmpty()) return Page.empty();

            List<StudentDTO> allDtos = coordinator.getLinkedFaculties().stream()
                    .flatMap(faculty -> {
                        var list = studentRepository.findByCollegeIdWithRelations(faculty.getId());
                        return list.stream()
                                .filter(s -> name == null || name.isBlank() || s.getName().toLowerCase().contains(name.toLowerCase()))
                                .map(this::convertToDTO);
                    }).toList();

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), allDtos.size());

            List<StudentDTO> pageContent = start > allDtos.size() ? Collections.emptyList() : allDtos.subList(start, end);

            return new PageImpl<>(pageContent, pageable, allDtos.size());
        }

        if (isSchoolCoordinator(role)) {
            Optional<Coordinator> optionalCoordinator = coordinatorRepository.findByEmailIgnoreCase(email);
            if (optionalCoordinator.isPresent() && optionalCoordinator.get().getSchool() != null) {
                Long schoolId = optionalCoordinator.get().getSchool().getId();
                List<Student> students = studentRepository.findBySchoolIdWithRelations(schoolId);

                if (name != null && !name.isBlank()) {
                    students = students.stream()
                            .filter(s -> s.getName() != null && s.getName().toLowerCase().contains(name.toLowerCase()))
                            .toList();
                }

                List<StudentDTO> dtos = students.stream().map(this::convertToDTO).toList();
                int size = pageable.getPageSize() == 0 ? 10 : pageable.getPageSize();
                int page = pageable.getPageNumber();
                int start = Math.min(page * size, dtos.size());
                int end = Math.min(start + size, dtos.size());

                List<StudentDTO> pagedDtos = dtos.subList(start, end);
                return new PageImpl<>(pagedDtos, PageRequest.of(page, size), dtos.size());
            }
            return Page.empty();
        }

        if (isTeacher(role)) {
            List<Student> students = studentRepository.findByTeacherEmailWithRelations(email);
            if (name != null && !name.isBlank()) {
                students = students.stream()
                        .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                        .toList();
            }
            return new PageImpl<>(students.stream().map(this::convertToDTO).toList(), pageable, students.size());
        }

        return Page.empty();
    }

    public List<StudentDTO> getStudentsByFaculty(Long facultyId) {
        List<Student> students = studentRepository.findByCollegeIdWithRelations(facultyId);
        return students.stream().map(this::convertToDTO).toList();
    }

    public List<StudentDTO> getStudentsBySchool(Long schoolId) {
        return studentRepository.findBySchoolIdWithRelations(schoolId)
                .stream()
                .map(this::convertToDTO)
                .toList();
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

    public Optional<Student> getStudentByEmailWithRelations(String email) {
        return studentRepository.findStudentWithRelationsByEmail(email);
    }

    public StudentDTO getAuthenticatedStudentDTO() {
        String email = getAuthenticatedEmail();
        Optional<Student> studentOpt = studentRepository.findStudentWithRelationsByEmail(email);
        if (studentOpt.isEmpty()) {
            throw new RuntimeException("Estudante autenticado não encontrado ou não possui vínculos.");
        }
        return convertToDTO(studentOpt.get());
    }

    public List<Student> getStudentsByHours(int maxHours) {
        return studentRepository.findByHoursPendingLessThanEqual(maxHours);
    }

    @Transactional
    public Student createStudent(Student student) {
        if (student.getPassword() == null || student.getPassword().isBlank()) {
            String defaultPassword = "123456"; // Você pode usar UUID ou lógica própria
            student.setPassword(passwordEncoder.encode(defaultPassword));
        } else {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }

        return studentRepository.save(student);
    }

    @Transactional
    public Student updateStudent(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante não encontrado com ID: " + id);
        }

        student.setId(id);
        if (!student.getPassword().startsWith("$2a$")) {
            student.setPassword(passwordEncoder.encode(student.getPassword()));
        }

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

        if (student.getSchool() == null) {
            throw new RuntimeException("Este estudante não possui escola vinculada ainda.");
        }

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

    private boolean isAdmin(String role) {
        return "ROLE_ADMIN".equals(role) || "ADMIN".equals(role);
    }

    private boolean isFacultyCoordinator(String role) {
        return "ROLE_COORDINATOR_FACULTY".equals(role)
                || "COORDINATOR_FACULTY".equals(role)
                || "FACULTY_COORDINATOR".equals(role);
    }

    private boolean isSchoolCoordinator(String role) {
        if (role == null) return false;

        return role.equalsIgnoreCase("ROLE_COORDINATOR_SCHOOL") ||
                role.equalsIgnoreCase("COORDINATOR_SCHOOL") ||
                role.equalsIgnoreCase("ROLE_SCHOOL_COORDINATOR") ||
                role.equalsIgnoreCase("SCHOOL_COORDINATOR");
    }

    private boolean isTeacher(String role) {
        return "ROLE_TEACHER".equals(role) || "TEACHER".equals(role);
    }
}
