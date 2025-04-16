package com.controller;

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
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true") // Conexão com frontend garantida
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * Retorna todos os estudantes cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> list = studentService.getAllStudents();
        return ResponseEntity.ok(list);
    }

    /**
     * Retorna um estudante específico pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retorna estudantes com horas pendentes menores ou iguais ao valor fornecido.
     */
    @GetMapping("/hours")
    public ResponseEntity<List<Student>> getStudentsByHours(@RequestParam int maxHours) {
        List<Student> students = studentService.getStudentsByHours(maxHours);
        if (students.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }

    /**
     * Cria um novo estudante.
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        Student created = studentService.createStudent(student);
        return ResponseEntity.status(201).body(created); // 201 Created
    }

    /**
     * Atualiza os dados de um estudante existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updated = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updated);
    }

    /**
     * Exclui um estudante pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
