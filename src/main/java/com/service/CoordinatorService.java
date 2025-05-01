package com.service;

import com.model.Coordinator;
import com.model.Faculty;
import com.model.School;
import com.model.User;
import com.repository.CoordinatorRepository;
import com.repository.FacultyRepository;
import com.repository.SchoolRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Cria um novo coordenador, validando vínculo com instituição e criando login.
     */
    public Coordinator createCoordinator(Coordinator coordinator) {
        validarDados(coordinator);

        Coordinator saved = coordinatorRepository.save(coordinator);

        // Criação automática de login
        if (userRepository.findByEmailIgnoreCase(coordinator.getEmail()).isEmpty()) {
            String papel = (coordinator.getFaculty() != null) ? "FACULTY_COORDINATOR" : "SCHOOL_COORDINATOR";
            User newUser = new User(
                    coordinator.getName(),
                    coordinator.getEmail(),
                    passwordEncoder.encode("123456"),
                    papel
            );
            userRepository.save(newUser);
        }

        return saved;
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
     * Retorna um coordenador pelo ID.
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
     * Retorna o coordenador atualmente autenticado (baseado no e-mail do token JWT).
     */
    public Coordinator getAuthenticatedCoordinator() {
        String email = getAuthenticatedEmail();

        return coordinatorRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado para o e-mail: " + email));
    }

    /**
     * Pega o e-mail do usuário autenticado via SecurityContext.
     */
    private String getAuthenticatedEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }

    /**
     * Valida as regras de vínculo do coordenador com faculdade ou escola.
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
