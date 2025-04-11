package com.controller;

import com.model.Teacher;
import com.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
// Ajuste importante: não pode usar "*" com credenciais habilitadas
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true") // ajuste conforme necessário
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * Cria um novo professor. O payload deve conter os IDs das escolas no campo `schools`.
     */
    @PostMapping
    public ResponseEntity<Teacher> createTeacher(@RequestBody Teacher teacher) {
        Teacher created = teacherService.createTeacher(teacher);
        return ResponseEntity.ok(created);
    }

    /**
     * Retorna a lista de todos os professores cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        List<Teacher> list = teacherService.getAllTeachers();
        return ResponseEntity.ok(list);
    }

    /**
     * Atualiza um professor pelo ID.
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
        return ResponseEntity.noContent().build();
    }
}
