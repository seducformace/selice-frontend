package com.controller;

import com.model.Coordinator;
import com.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller responsável pelos endpoints de coordenadores.
 */
@RestController
@RequestMapping("/api/coordinators")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CoordinatorController {

    @Autowired
    private CoordinatorService coordinatorService;

    /**
     * Cria um novo coordenador.
     */
    @PostMapping
    public ResponseEntity<Object> createCoordinator(@RequestBody Coordinator coordinator) {
        try {
            Coordinator newCoordinator = coordinatorService.createCoordinator(coordinator);
            return ResponseEntity.status(201).body(newCoordinator);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar coordenador.");
        }
    }

    /**
     * Lista todos os coordenadores cadastrados.
     */
    @GetMapping
    public ResponseEntity<List<Coordinator>> getAllCoordinators() {
        return ResponseEntity.ok(coordinatorService.getAllCoordinators());
    }

    /**
     * Retorna um coordenador pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCoordinatorById(@PathVariable Long id) {
        return coordinatorService.getCoordinatorById(id)
                .<ResponseEntity<Object>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Coordenador não encontrado com ID: " + id));
    }

    /**
     * Atualiza um coordenador existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCoordinator(@PathVariable Long id, @RequestBody Coordinator coordinator) {
        try {
            Coordinator updatedCoordinator = coordinatorService.updateCoordinator(id, coordinator);
            return ResponseEntity.ok(updatedCoordinator);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar coordenador.");
        }
    }

    /**
     * Exclui um coordenador pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCoordinator(@PathVariable Long id) {
        try {
            coordinatorService.deleteCoordinator(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao excluir coordenador.");
        }
    }
}
