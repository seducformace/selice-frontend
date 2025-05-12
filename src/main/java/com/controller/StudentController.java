package com.controller;

import com.dto.StudentDTO;
import com.model.Student;
import com.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * Retorna todos os estudantes com base no papel do usuário autenticado.
     * Pode retornar de forma paginada ou completa, com ou sem filtro por nome.
     */
    @GetMapping
    public Object getStudents(
            @RequestParam(required = false, defaultValue = "false") boolean paged,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String name
    ) {
        if (paged) {
            Pageable pageable = PageRequest.of(page, size);
            return studentService.getAllStudentsPaginated(pageable, name);
        } else {
            return studentService.getAllStudentsAsDTO();
        }
    }

    /**
     * Retorna um estudante específico pelo ID.
     */
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
    }

    /**
     * Cria um novo estudante.
     */
    @PostMapping
    public Student createStudent(@RequestBody @Valid Student student) {
        return studentService.createStudent(student);
    }

    /**
     * Atualiza um estudante existente.
     */
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody @Valid Student student) {
        return studentService.updateStudent(id, student);
    }

    /**
     * Exclui um estudante pelo ID.
     */
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    /**
     * Associa um estudante a uma escola (para coordenador escolar).
     */
    @PutMapping("/{id}/assign-school")
    public void assignStudentToSchool(@PathVariable Long id, @RequestBody AssignSchoolRequest request) {
        studentService.assignStudentToSchool(id, request.getSchoolId());
    }

    /**
     * DTO interno para requisição de associação de escola.
     */
    public static class AssignSchoolRequest {
        private Long schoolId;

        public Long getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(Long schoolId) {
            this.schoolId = schoolId;
        }
    }
}
