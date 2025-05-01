package com.controller;

import com.model.Coordinator;
import com.service.CoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coordinators")
@Validated
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class CoordinatorController {

    @Autowired
    private CoordinatorService coordinatorService;

    /**
     * Lista todos os coordenadores.
     */
    @GetMapping
    public ResponseEntity<List<Coordinator>> getAllCoordinators() {
        List<Coordinator> list = coordinatorService.getAllCoordinators();
        return ResponseEntity.ok(list);
    }

    /**
     * Retorna um coordenador pelo ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Coordinator> getCoordinatorById(@PathVariable Long id) {
        return coordinatorService.getCoordinatorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cria um novo coordenador.
     */
    @PostMapping
    public ResponseEntity<?> createCoordinator(@RequestBody Coordinator coordinator) {
        try {
            Coordinator created = coordinatorService.createCoordinator(coordinator);
            return ResponseEntity.status(201).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao criar o coordenador.");
        }
    }

    /**
     * Atualiza um coordenador existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoordinator(@PathVariable Long id, @RequestBody Coordinator coordinator) {
        try {
            Coordinator updated = coordinatorService.updateCoordinator(id, coordinator);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar o coordenador.");
        }
    }

    /**
     * Deleta um coordenador pelo ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinator(@PathVariable Long id) {
        try {
            coordinatorService.deleteCoordinator(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retorna o coordenador atualmente autenticado (com base no JWT).
     */
    @GetMapping("/me")
    public ResponseEntity<?> getAuthenticatedCoordinator() {
        try {
            Coordinator coordinator = coordinatorService.getAuthenticatedCoordinator();
            return ResponseEntity.ok(coordinator);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Coordenador autenticado não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao buscar o coordenador autenticado.");
        }
    }
}
