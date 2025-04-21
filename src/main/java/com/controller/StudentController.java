package com.controller;

import com.dto.StudentDTO;
import com.model.Student;
import com.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * Lista todos os estudantes como DTOs com dados relacionados (faculdade, escola, professor).
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        try {
            List<StudentDTO> list = studentService.getAllStudentsAsDTO();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retorna um estudante pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista estudantes com horas pendentes menores ou iguais ao valor informado.
     */
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

    /**
     * Cria um novo estudante.
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        try {
            Student created = studentService.createStudent(student);
            return ResponseEntity.status(201).body(created);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Atualiza os dados de um estudante existente.
     */
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

    /**
     * Remove um estudante existente pelo ID.
     */
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
}
