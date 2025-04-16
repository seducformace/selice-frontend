package com.controller;

import com.model.Teacher;
import com.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações com professores.
 */
@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * Cria um novo professor.
     * Espera um JSON com os campos obrigatórios e ao menos uma escola vinculada.
     */
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher created = teacherService.createTeacher(teacher);
        return ResponseEntity.status(201).body(created); // 201 Created
    }

    /**
     * Retorna todos os professores cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> list = teacherService.getAllTeachers();
        return ResponseEntity.ok(list);
    }

    /**
     * Atualiza um professor existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher updated = teacherService.updateTeacher(id, teacher);
        return ResponseEntity.ok(updated);
    }

    /**
     * Exclui um professor pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
