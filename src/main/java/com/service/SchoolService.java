package com.service;

import com.model.School;
import com.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável por gerenciar a lógica de negócios relacionada às escolas.
 * Aqui é onde as regras do jogo são definidas e aplicadas! 🚀
 */
@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Retorna todas as escolas sem paginação.
     *
     * @return Lista de todas as escolas.
     */
    public List<School> getAllSchoolsWithoutPagination() {
        return schoolRepository.findAll();
    }

    /**
     * Retorna todas as escolas com paginação e ordenação.
     *
     * @param pageable Configuração de paginação e ordenação.
     * @return Página contendo as escolas.
     */
    public Page<School> getAllSchools(Pageable pageable) {
        return schoolRepository.findAll(pageable);
    }

    /**
     * Busca escolas pelo nome (contém, ignorando maiúsculas/minúsculas).
     *
     * @param name Nome ou parte do nome da escola.
     * @return Lista de escolas encontradas.
     */
    public List<School> searchByName(String name) {
        return schoolRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Busca uma escola pelo ID.
     *
     * @param id ID da escola.
     * @return Optional contendo a escola encontrada ou vazio caso não exista.
     */
    public Optional<School> getSchoolById(Long id) {
        return schoolRepository.findById(id);
    }

    /**
     * Cadastra uma nova escola.
     *
     * @param school Objeto School a ser cadastrado.
     * @return Escola criada.
     */
    public School createSchool(School school) {
        if (school.getName() == null || school.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome da escola é obrigatório.");
        }
        return schoolRepository.save(school);
    }

    /**
     * Atualiza uma escola existente.
     *
     * @param id     ID da escola a ser atualizada.
     * @param school Dados atualizados da escola.
     * @return Escola atualizada.
     * @throws RuntimeException Caso a escola não seja encontrada.
     */
    public School updateSchool(Long id, School school) {
        return schoolRepository.findById(id)
                .map(existingSchool -> {
                    if (school.getName() != null) {
                        existingSchool.setName(school.getName());
                    }
                    if (school.getAddress() != null) {
                        existingSchool.setAddress(school.getAddress());
                    }
                    if (school.getCity() != null) {
                        existingSchool.setCity(school.getCity());
                    }
                    if (school.getState() != null) {
                        existingSchool.setState(school.getState());
                    }
                    return schoolRepository.save(existingSchool);
                })
                .orElseThrow(() -> new RuntimeException("Escola não encontrada com ID: " + id));
    }

    /**
     * Exclui uma escola pelo ID.
     *
     * @param id ID da escola a ser excluída.
     * @throws RuntimeException Caso a escola não seja encontrada.
     */
    public void deleteSchool(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new RuntimeException("Escola não encontrada com ID: " + id);
        }
        schoolRepository.deleteById(id);
    }
}
