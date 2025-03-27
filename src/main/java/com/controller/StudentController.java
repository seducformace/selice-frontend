package com.controller;

import com.model.Student;
import com.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/hours")
    public ResponseEntity<List<Student>> getStudentsByHours(@RequestParam int maxHours) {
        System.out.println("Controller: Recebido maxHours: " + maxHours);
        List<Student> students = studentService.getStudentsByHours(maxHours);
        System.out.println("Controller: Estudantes retornados pelo Service: " + students);

        if (students.isEmpty()) {
            System.out.println("Controller: Nenhum estudante encontrado com horas pendentes <= " + maxHours);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(students);
    }
}
