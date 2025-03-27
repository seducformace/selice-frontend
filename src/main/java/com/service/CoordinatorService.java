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
 * Serviço responsável por encapsular a lógica de negócio para coordenadores.
 * Aqui acontece a lógica principal, como cadastrar, buscar, excluir ou atualizar coordenadores.
 */
@Service
public class CoordinatorService {

    private static final Logger LOGGER = Logger.getLogger(CoordinatorService.class.getName());

    @Autowired
    private CoordinatorRepository coordinatorRepository; // Nosso fiel escudeiro para acessar o banco de dados!

    /**
     * Cadastrar um novo coordenador.
     * Estamos adicionando mais um guia ao sistema!
     */
    public Coordinator createCoordinator(Coordinator coordinator) {
        if (coordinator.getSchool() == null && coordinator.getCollege() == null) {
            throw new IllegalArgumentException("O coordenador deve estar vinculado a uma escola ou faculdade.");
        }

        // Verificar se o e-mail já está em uso
        if (coordinatorRepository.findByEmail(coordinator.getEmail()).isPresent()) {
            throw new DataIntegrityViolationException("Já existe um coordenador com o e-mail " + coordinator.getEmail());
        }

        LOGGER.info("Criando um novo coordenador: " + coordinator.getName());
        return coordinatorRepository.save(coordinator);
    }

    /**
     * Buscar todos os coordenadores.
     */
    public List<Coordinator> getAllCoordinators() {
        LOGGER.info("Buscando todos os coordenadores");
        return coordinatorRepository.findAll();
    }

    /**
     * Buscar coordenador por ID.
     */
    public Optional<Coordinator> getCoordinatorById(Long id) {
        LOGGER.info("Buscando coordenador com ID: " + id);
        return coordinatorRepository.findById(id);
    }

    /**
     * Buscar coordenador pelo e-mail.
     */
    public Optional<Coordinator> getCoordinatorByEmail(String email) {
        LOGGER.info("Buscando coordenador com e-mail: " + email);
        return coordinatorRepository.findByEmail(email);
    }

    /**
     * Excluir um coordenador por ID.
     */
    public void deleteCoordinator(Long id) {
        if (!coordinatorRepository.existsById(id)) {
            throw new IllegalArgumentException("Coordenador não encontrado com o ID " + id);
        }
        LOGGER.info("Excluindo coordenador com ID: " + id);
        coordinatorRepository.deleteById(id);
    }

    /**
     * Atualizar as informações de um coordenador.
     */
    public Coordinator updateCoordinator(Long id, Coordinator updatedCoordinator) {
        Coordinator existingCoordinator = coordinatorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado com o ID " + id));

        // Atualizar apenas os campos necessários
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
