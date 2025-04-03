package com.service;

import com.model.Coordinator;
import com.repository.CoordinatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Serviço responsável por encapsular a lógica de negócio para Coordenadores.
 * Realiza operações como criação, consulta, atualização e remoção.
 */
@Service
public class CoordinatorService {

    private static final Logger LOGGER = Logger.getLogger(CoordinatorService.class.getName());

    @Autowired
    private CoordinatorRepository coordinatorRepository;

    /**
     * Cadastra um novo coordenador.
     * Realiza validações obrigatórias antes de persistir os dados.
     */
    public Coordinator createCoordinator(Coordinator coordinator) {
        // Verifica se o coordenador está vinculado a uma instituição
        if (coordinator.getSchool() == null && coordinator.getCollege() == null) {
            throw new IllegalArgumentException("O coordenador deve estar vinculado a uma escola ou faculdade.");
        }

        // Garante que não haja duplicidade de e-mail
        if (coordinatorRepository.findByEmail(coordinator.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um coordenador com o e-mail " + coordinator.getEmail());
        }

        LOGGER.info("Criando novo coordenador: " + coordinator.getName());
        return coordinatorRepository.save(coordinator);
    }

    /**
     * Retorna todos os coordenadores cadastrados.
     */
    public List<Coordinator> getAllCoordinators() {
        LOGGER.info("Buscando todos os coordenadores");
        return coordinatorRepository.findAll();
    }

    /**
     * Busca um coordenador específico pelo ID.
     */
    public Optional<Coordinator> getCoordinatorById(Long id) {
        LOGGER.info("Buscando coordenador com ID: " + id);
        return coordinatorRepository.findById(id);
    }

    /**
     * Busca um coordenador pelo e-mail.
     */
    public Optional<Coordinator> getCoordinatorByEmail(String email) {
        LOGGER.info("Buscando coordenador com e-mail: " + email);
        return coordinatorRepository.findByEmail(email);
    }

    /**
     * Remove um coordenador pelo ID.
     */
    public void deleteCoordinator(Long id) {
        if (!coordinatorRepository.existsById(id)) {
            throw new IllegalArgumentException("Coordenador não encontrado com o ID " + id);
        }

        LOGGER.info("Excluindo coordenador com ID: " + id);
        coordinatorRepository.deleteById(id);
    }

    /**
     * Atualiza os dados de um coordenador existente.
     */
    public Coordinator updateCoordinator(Long id, Coordinator updatedCoordinator) {
        Coordinator existingCoordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com o ID " + id));

        existingCoordinator.setName(updatedCoordinator.getName());
        existingCoordinator.setEmail(updatedCoordinator.getEmail());
        existingCoordinator.setPhoneNumber(updatedCoordinator.getPhoneNumber());
        existingCoordinator.setDepartment(updatedCoordinator.getDepartment());
        existingCoordinator.setSchool(updatedCoordinator.getSchool());
        existingCoordinator.setCollege(updatedCoordinator.getCollege());

        LOGGER.info("Atualizando coordenador com ID: " + id);
        return coordinatorRepository.save(existingCoordinator);
    }
}
