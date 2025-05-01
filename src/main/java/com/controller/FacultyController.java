package com.controller;

import com.dto.ReportsDTO;
import com.model.Course;
import com.model.Faculty;
import com.repository.CourseRepository;
import com.service.FacultyService;
import com.service.ReportsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST responsável por expor os endpoints de operações
 * relacionadas às faculdades cadastradas no sistema SELICE.
 */
@RestController
@RequestMapping("/api/faculties")
public class FacultyController {

    @Autowired
    private FacultyService facultyService;

    @Autowired
    private ReportsService reportsService;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Cadastra uma nova faculdade no sistema.
     * Endpoint: POST /api/faculties
     */
    @PostMapping
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        Faculty newFaculty = facultyService.createFaculty(faculty);
        return ResponseEntity.ok(newFaculty);
    }

    /**
     * Retorna todas as faculdades cadastradas.
     * Endpoint: GET /api/faculties
     */
    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        try {
            List<Faculty> faculties = facultyService.getAllFaculties();

            // Log para debug no console
            System.out.println("Faculdades carregadas: " + faculties.size());
            faculties.forEach(f -> System.out.println("-> " + f.getName()));

            return ResponseEntity.ok(faculties);
        } catch (Exception e) {
            System.err.println("Erro ao buscar faculdades:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Busca uma faculdade específica pelo ID.
     * Endpoint: GET /api/faculties/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable Long id) {
        Optional<Faculty> faculty = facultyService.getFacultyById(id);
        return faculty.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Remove uma faculdade pelo ID.
     * Endpoint: DELETE /api/faculties/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualiza uma faculdade existente.
     * Endpoint: PUT /api/faculties/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@PathVariable Long id, @RequestBody Faculty updatedFaculty) {
        try {
            Faculty saved = facultyService.updateFaculty(id, updatedFaculty);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Retorna estatísticas de faculdades ativas/inativas e por tipo.
     * Endpoint: GET /api/faculties/report
     */
    @GetMapping("/report")
    public ResponseEntity<ReportsDTO> getFacultyStatistics() {
        ReportsDTO report = reportsService.getFacultiesStats();
        return ResponseEntity.ok(report);
    }

    /**
     * Endpoint de teste básico para validar funcionamento do controller.
     * Endpoint: GET /api/faculties/test
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("FacultyController is up and running!");
    }

    /**
     * Retorna os cursos vinculados a uma faculdade específica.
     * Endpoint: GET /api/faculties/{id}/courses
     */
    @GetMapping("/{id}/courses")
    public ResponseEntity<List<Course>> getCoursesByFaculty(@PathVariable Long id) {
        Optional<Faculty> facultyOpt = facultyService.getFacultyById(id);
        if (facultyOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<Course> courses = courseRepository.findByFacultyId(id);
        return ResponseEntity.ok(courses);
    }
}
