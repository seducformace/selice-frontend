package com.service;

import com.model.Faculty;
import com.model.Coordinator;
import com.model.School;
import com.repository.FacultyRepository;
import com.repository.CoordinatorRepository;
import com.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos Coordenadores.
 */
@Service
public class CoordinatorService {

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Cria um novo coordenador, validando vínculo com instituição.
     */
    public Coordinator createCoordinator(Coordinator coordinator) {
        validarDados(coordinator);
        return coordinatorRepository.save(coordinator);
    }

    /**
     * Atualiza um coordenador existente.
     */
    public Coordinator updateCoordinator(Long id, Coordinator updated) {
        Coordinator existing = coordinatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com ID: " + id));

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setCpf(updated.getCpf());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setDepartment(updated.getDepartment());
        existing.setStatus(updated.getStatus());
        existing.setFaculty(updated.getFaculty());
        existing.setSchool(updated.getSchool());

        validarDados(existing);
        return coordinatorRepository.save(existing);
    }

    /**
     * Retorna todos os coordenadores cadastrados.
     */
    public List<Coordinator> getAllCoordinators() {
        return coordinatorRepository.findAll();
    }

    /**
     * Retorna um coordenador específico pelo ID.
     */
    public Optional<Coordinator> getCoordinatorById(Long id) {
        return coordinatorRepository.findById(id);
    }

    /**
     * Exclui um coordenador pelo ID.
     */
    public void deleteCoordinator(Long id) {
        if (!coordinatorRepository.existsById(id)) {
            throw new IllegalArgumentException("Coordenador não encontrado com ID: " + id);
        }
        coordinatorRepository.deleteById(id);
    }

    /**
     * Verifica se os vínculos com instituições são válidos.
     */
    private void validarDados(Coordinator coordinator) {
        if (coordinator.getFaculty() != null && coordinator.getSchool() != null) {
            throw new IllegalArgumentException("O coordenador não pode estar vinculado a uma faculdade e uma escola ao mesmo tempo.");
        }

        if (coordinator.getFaculty() == null && coordinator.getSchool() == null) {
            throw new IllegalArgumentException("O coordenador deve estar vinculado a uma faculdade ou escola.");
        }

        if (coordinator.getFaculty() != null) {
            Long facultyId = coordinator.getFaculty().getId();
            facultyRepository.findById(facultyId).orElseThrow(() ->
                    new DataIntegrityViolationException("Faculdade com ID " + facultyId + " não existe."));
        }

        if (coordinator.getSchool() != null) {
            Long schoolId = coordinator.getSchool().getId();
            schoolRepository.findById(schoolId).orElseThrow(() ->
                    new DataIntegrityViolationException("Escola com ID " + schoolId + " não existe."));
        }
    }
}
