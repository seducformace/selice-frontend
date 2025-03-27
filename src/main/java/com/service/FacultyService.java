package com.service;

import com.model.Faculty;
import com.repository.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelas regras de negócio relacionadas às faculdades.
 */
@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    /**
     * Cadastra uma nova faculdade no banco de dados.
     */
    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    /**
     * Retorna todas as faculdades cadastradas.
     */
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }

    /**
     * Busca uma faculdade pelo ID.
     */
    public Optional<Faculty> getFacultyById(Long id) {
        return facultyRepository.findById(id);
    }

    /**
     * Remove uma faculdade pelo ID.
     */
    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    /**
     * Atualiza os dados de uma faculdade existente.
     */
    public Faculty updateFaculty(Long id, Faculty updatedFaculty) {
        System.out.println("Atualizando faculdade ID: " + id);

        Faculty existing = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculdade não encontrada com ID: " + id));

        existing.setName(updatedFaculty.getName());
        existing.setMecCode(updatedFaculty.getMecCode());
        existing.setCnpj(updatedFaculty.getCnpj());
        existing.setDeanName(updatedFaculty.getDeanName());
        existing.setPartnershipResponsible(updatedFaculty.getPartnershipResponsible());
        existing.setContactPhone(updatedFaculty.getContactPhone());
        existing.setCity(updatedFaculty.getCity());
        existing.setState(updatedFaculty.getState());
        existing.setOfferedCourses(updatedFaculty.getOfferedCourses());

        return facultyRepository.save(existing);
    }

}
