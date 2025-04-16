package com.service;

import com.model.School;
import com.model.Teacher;
import com.repository.SchoolRepository;
import com.repository.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócios dos professores.
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Cria um novo professor, garantindo que esteja vinculado a pelo menos uma escola válida.
     */
    public Teacher createTeacher(Teacher teacher) {
        if (teacher.getSchools() == null || teacher.getSchools().isEmpty()) {
            throw new IllegalArgumentException("O professor deve estar vinculado a pelo menos uma escola.");
        }

        List<School> validSchools = new ArrayList<>();
        for (School school : teacher.getSchools()) {
            School persisted = schoolRepository.findById(school.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Escola com ID " + school.getId() + " não encontrada."));
            validSchools.add(persisted);
        }

        teacher.setSchools(validSchools);
        return teacherRepository.save(teacher);
    }

    /**
     * Atualiza os dados de um professor existente.
     */
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor com ID " + id + " não encontrado."));

        existing.setName(updatedTeacher.getName());
        existing.setEmail(updatedTeacher.getEmail());
        existing.setCpf(updatedTeacher.getCpf());
        existing.setRegistration(updatedTeacher.getRegistration());
        existing.setQualification(updatedTeacher.getQualification());
        existing.setDiscipline(updatedTeacher.getDiscipline());
        existing.setOrientedStudents(updatedTeacher.getOrientedStudents());
        existing.setStudentsInProgress(updatedTeacher.getStudentsInProgress());

        List<School> updatedSchools = new ArrayList<>();
        for (School school : updatedTeacher.getSchools()) {
            School persisted = schoolRepository.findById(school.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Escola com ID " + school.getId() + " não encontrada."));
            updatedSchools.add(persisted);
        }

        existing.setSchools(updatedSchools);
        return teacherRepository.save(existing);
    }

    /**
     * Retorna todos os professores cadastrados.
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Exclui um professor pelo ID.
     */
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new EntityNotFoundException("Professor com ID " + id + " não encontrado.");
        }
        teacherRepository.deleteById(id);
    }
}
