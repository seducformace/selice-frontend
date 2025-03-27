package com.controller;

import com.model.Coordinator;
import com.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelos endpoints de coordenadores.
 */
@RestController
@RequestMapping("/api/coordinators")
public class CoordinatorController {

    @Autowired
    private CoordinatorService coordinatorService;

    @PostMapping
    public ResponseEntity<Coordinator> createCoordinator(@RequestBody Coordinator coordinator) {
        Coordinator newCoordinator = coordinatorService.createCoordinator(coordinator);
        return ResponseEntity.ok(newCoordinator);
    }

    @GetMapping
    public ResponseEntity<List<Coordinator>> getAllCoordinators() {
        return ResponseEntity.ok(coordinatorService.getAllCoordinators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coordinator> getCoordinatorById(@PathVariable Long id) {
        return coordinatorService.getCoordinatorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coordinator> updateCoordinator(@PathVariable Long id, @RequestBody Coordinator coordinator) {
        try {
            Coordinator updatedCoordinator = coordinatorService.updateCoordinator(id, coordinator);
            return ResponseEntity.ok(updatedCoordinator);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinator(@PathVariable Long id) {
        coordinatorService.deleteCoordinator(id);
        return ResponseEntity.noContent().build();
    }
}
