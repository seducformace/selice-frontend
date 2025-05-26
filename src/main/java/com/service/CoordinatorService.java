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

    public Coordinator createCoordinator(Coordinator coordinator) {
        validarDados(coordinator);

        // Set vínculos principais (escola ou faculdade)
        if (coordinator.getFaculty() != null && coordinator.getFaculty().getId() != null) {
            Faculty faculty = facultyRepository.findById(coordinator.getFaculty().getId())
                    .orElseThrow(() -> new DataIntegrityViolationException("Faculdade não encontrada."));
            coordinator.setFaculty(faculty);
            coordinator.setSchool(null);
            coordinator.setLinkedFaculties(null);
        } else if (coordinator.getSchool() != null && coordinator.getSchool().getId() != null) {
            School school = schoolRepository.findById(coordinator.getSchool().getId())
                    .orElseThrow(() -> new DataIntegrityViolationException("Escola não encontrada."));
            coordinator.setSchool(school);
            coordinator.setFaculty(null);

            // Set faculdades vinculadas
            if (coordinator.getLinkedFaculties() != null && !coordinator.getLinkedFaculties().isEmpty()) {
                List<Faculty> linked = coordinator.getLinkedFaculties().stream()
                        .map(fac -> facultyRepository.findById(fac.getId())
                                .orElseThrow(() -> new DataIntegrityViolationException("Faculdade vinculada não encontrada: ID " + fac.getId())))
                        .toList();
                coordinator.setLinkedFaculties(linked);
            }
        }

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

    public Coordinator updateCoordinator(Long id, Coordinator updated) {
        Coordinator existing = coordinatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com ID: " + id));

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setCpf(updated.getCpf());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setDepartment(updated.getDepartment());
        existing.setStatus(updated.getStatus());

        // Atualizar vínculo principal
        if (updated.getFaculty() != null && updated.getFaculty().getId() != null) {
            Faculty faculty = facultyRepository.findById(updated.getFaculty().getId())
                    .orElseThrow(() -> new DataIntegrityViolationException("Faculdade não encontrada."));
            existing.setFaculty(faculty);
            existing.setSchool(null);
            existing.setLinkedFaculties(null);
        } else if (updated.getSchool() != null && updated.getSchool().getId() != null) {
            School school = schoolRepository.findById(updated.getSchool().getId())
                    .orElseThrow(() -> new DataIntegrityViolationException("Escola não encontrada."));
            existing.setSchool(school);
            existing.setFaculty(null);

            // Atualizar faculdades vinculadas
            if (updated.getLinkedFaculties() != null && !updated.getLinkedFaculties().isEmpty()) {
                List<Faculty> linked = updated.getLinkedFaculties().stream()
                        .map(fac -> facultyRepository.findById(fac.getId())
                                .orElseThrow(() -> new DataIntegrityViolationException("Faculdade vinculada não encontrada: ID " + fac.getId())))
                        .toList();
                existing.setLinkedFaculties(linked);
            } else {
                existing.setLinkedFaculties(null);
            }
        }

        validarDados(existing);
        return coordinatorRepository.save(existing);
    }

    public List<Coordinator> getAllCoordinators() {
        return coordinatorRepository.findAll();
    }

    public Optional<Coordinator> getCoordinatorById(Long id) {
        return coordinatorRepository.findById(id);
    }

    public void deleteCoordinator(Long id) {
        if (!coordinatorRepository.existsById(id)) {
            throw new IllegalArgumentException("Coordenador não encontrado com ID: " + id);
        }
        coordinatorRepository.deleteById(id);
    }

    public Coordinator getAuthenticatedCoordinator() {
        String email = getAuthenticatedEmail();

        return coordinatorRepository.findWithLinkedFaculties(email)
                .map(coordinator -> {
                    // Compatibiliza a leitura para componentes que esperam getFaculty()
                    if ((coordinator.getFaculty() == null || coordinator.getFaculty().getId() == null) &&
                            coordinator.getLinkedFaculties() != null &&
                            !coordinator.getLinkedFaculties().isEmpty()) {
                        coordinator.setFaculty(coordinator.getLinkedFaculties().get(0));
                    }
                    return coordinator;
                })
                .orElseThrow(() -> new RuntimeException("Coordenador não encontrado para o e-mail: " + email));
    }

    private String getAuthenticatedEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return principal.toString();
        }
    }

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
