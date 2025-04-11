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

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    /**
     * Cria um novo professor, garantindo que todas as escolas estejam corretamente associadas.
     */
    public Teacher createTeacher(Teacher teacher) {
        // Regra de negócio: pelo menos uma escola vinculada
        if (teacher.getSchools() == null || teacher.getSchools().isEmpty()) {
            throw new IllegalArgumentException("O professor deve estar vinculado a pelo menos uma escola.");
        }

        // Confirma se cada escola existe no banco
        List<School> schools = new ArrayList<>();
        for (School s : teacher.getSchools()) {
            School persistedSchool = schoolRepository.findById(s.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Escola com ID " + s.getId() + " não encontrada."));
            schools.add(persistedSchool);
        }

        teacher.setSchools(schools);
        return teacherRepository.save(teacher);
    }

    /**
     * Atualiza os dados de um professor.
     */
    public Teacher updateTeacher(Long id, Teacher updatedTeacher) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Professor com ID " + id + " não encontrado."));

        existing.setName(updatedTeacher.getName());
        existing.setEmail(updatedTeacher.getEmail());
        existing.setRegistration(updatedTeacher.getRegistration());
        existing.setQualification(updatedTeacher.getQualification());
        existing.setDiscipline(updatedTeacher.getDiscipline());
        existing.setOrientedStudents(updatedTeacher.getOrientedStudents());
        existing.setStudentsInProgress(updatedTeacher.getStudentsInProgress());

        // Atualiza lista de escolas vinculadas
        List<School> schools = new ArrayList<>();
        for (School s : updatedTeacher.getSchools()) {
            School persistedSchool = schoolRepository.findById(s.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Escola com ID " + s.getId() + " não encontrada."));
            schools.add(persistedSchool);
        }

        existing.setSchools(schools);
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
