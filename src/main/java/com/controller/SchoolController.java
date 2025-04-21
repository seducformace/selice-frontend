package com.controller;

import com.model.School;
import com.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    /**
     * Retorna todas as escolas sem paginação.
     */
    @GetMapping
    public ResponseEntity<List<School>> getAllSchoolsWithoutPagination() {
        List<School> schools = schoolService.getAllSchoolsWithoutPagination();
        if (schools.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(schools);
    }

    /**
     * Retorna todas as escolas com paginação e ordenação.
     */
    @GetMapping("/paged")
    public ResponseEntity<Page<School>> getAllSchools(Pageable pageable) {
        Page<School> schools = schoolService.getAllSchools(pageable);
        if (schools.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(schools);
    }

    /**
     * Retorna uma escola pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<School> getSchoolById(@PathVariable Long id) {
        Optional<School> schoolOptional = schoolService.getSchoolById(id);
        return schoolOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cadastra uma nova escola, incluindo campos como ideb, latitude e longitude.
     */
    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody School school) {
        try {
            School newSchool = schoolService.createSchool(school);
            return ResponseEntity.status(201).body(newSchool);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Atualiza uma escola existente (incluindo ideb, latitude, longitude se vierem no JSON).
     */
    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable Long id, @RequestBody School school) {
        try {
            School updatedSchool = schoolService.updateSchool(id, school);
            return ResponseEntity.ok(updatedSchool);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Exclui uma escola pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        try {
            schoolService.deleteSchool(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
