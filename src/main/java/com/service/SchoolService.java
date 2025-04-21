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
 */
@Service
public class SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Retorna todas as escolas sem paginação.
     */
    public List<School> getAllSchoolsWithoutPagination() {
        return schoolRepository.findAll();
    }

    /**
     * Retorna todas as escolas com paginação e ordenação.
     */
    public Page<School> getAllSchools(Pageable pageable) {
        return schoolRepository.findAll(pageable);
    }

    /**
     * Busca escolas pelo nome.
     */
    public List<School> searchByName(String name) {
        return schoolRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * Busca uma escola pelo ID.
     */
    public Optional<School> getSchoolById(Long id) {
        return schoolRepository.findById(id);
    }

    /**
     * Cadastra uma nova escola.
     */
    public School createSchool(School school) {
        if (school.getName() == null || school.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome da escola é obrigatório.");
        }
        return schoolRepository.save(school);
    }

    /**
     * Atualiza uma escola existente.
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
                    if (school.getType() != null) {
                        existingSchool.setType(school.getType());
                    }
                    if (school.getStatus() != null) {
                        existingSchool.setStatus(school.getStatus());
                    }
                    if (school.getPhone() != null) {
                        existingSchool.setPhone(school.getPhone());
                    }
                    if (school.getEmail() != null) {
                        existingSchool.setEmail(school.getEmail());
                    }
                    if (school.getInepCode() != null) {
                        existingSchool.setInepCode(school.getInepCode());
                    }
                    if (school.getIdeb() != null) {
                        existingSchool.setIdeb(school.getIdeb());
                    }
                    if (school.getLatitude() != null) {
                        existingSchool.setLatitude(school.getLatitude());
                    }
                    if (school.getLongitude() != null) {
                        existingSchool.setLongitude(school.getLongitude());
                    }

                    return schoolRepository.save(existingSchool);
                })
                .orElseThrow(() -> new RuntimeException("Escola não encontrada com ID: " + id));
    }

    /**
     * Exclui uma escola pelo ID.
     */
    public void deleteSchool(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new RuntimeException("Escola não encontrada com ID: " + id);
        }
        schoolRepository.deleteById(id);
    }
}
