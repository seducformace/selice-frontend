package com.controller;

import com.dto.StudentDTO;
import com.model.Student;
import com.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

/**
 * Controlador REST para operações com estudantes.
 */
@RestController
@RequestMapping("/api/students")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * Lista estudantes conforme a role do usuário autenticado:
     *
     * - ADMIN: vê todos, ou filtra por faculdade ou escola
     * - COORDINATOR_FACULTY: vê apenas os alunos de sua faculdade
     * - COORDINATOR_SCHOOL: vê apenas os alunos vinculados à sua escola
     * - TEACHER: (⚠️ ainda não implementado aqui, mas previsto)
     * - STUDENT: não acessa essa rota
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents(
            @RequestParam(required = false) Long collegeId,
            @RequestParam(required = false) Long schoolId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        try {
            List<StudentDTO> students;

            // Pega a role do usuário logado
            String role = userDetails.getAuthorities().iterator().next().getAuthority();
            String email = userDetails.getUsername();

            // ADMIN pode ver tudo, com ou sem filtro por parâmetro
            if ("ROLE_ADMIN".equals(role)) {
                if (collegeId != null) {
                    students = studentService.getStudentsByCollegeIdAsDTO(collegeId);
                } else if (schoolId != null) {
                    students = studentService.getStudentsBySchoolIdAsDTO(schoolId);
                } else {
                    students = studentService.getAllStudentsAsDTO();
                }

                // COORDENADOR DE FACULDADE vê apenas os estudantes vinculados à sua instituição
            } else if ("ROLE_COORDINATOR_FACULTY".equals(role)) {
                students = studentService.getStudentsByCoordinatorFaculty(email);

                // COORDENADOR DE ESCOLA vê apenas os estudantes alocados em sua escola
            } else if ("ROLE_COORDINATOR_SCHOOL".equals(role)) {
                students = studentService.getStudentsByCoordinatorSchool(email);

                // TEACHER (⚠️ Implementar no service caso necessário)
            } else if ("ROLE_TEACHER".equals(role)) {
                students = studentService.getStudentsByTeacher(email);

                // Outras roles não têm acesso
            } else {
                students = Collections.emptyList();
            }

            return ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hours")
    public ResponseEntity<List<Student>> getStudentsByHours(@RequestParam int maxHours) {
        try {
            List<Student> students = studentService.getStudentsByHours(maxHours);
            return students.isEmpty()
                    ? ResponseEntity.noContent().build()
                    : ResponseEntity.ok(students);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student created = studentService.createStudent(student);
            return ResponseEntity.status(201).body(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            Student updated = studentService.updateStudent(id, student);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retorna os dados do estudante autenticado com base no token JWT.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedStudent() {
        try {
            Student student = studentService.getAuthenticatedStudent();
            return ResponseEntity.ok(student);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Estudante autenticado não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar o estudante autenticado.");
        }
    }
}
