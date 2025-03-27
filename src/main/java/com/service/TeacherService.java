package com.service;

import com.model.Teacher;
import com.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pelo gerenciamento de professores no sistema.
 */
@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    /**
     * Cria um novo professor.
     *
     * @param teacher Objeto do professor a ser criado.
     * @return O professor criado.
     */
    public Teacher createTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    /**
     * Busca todos os professores cadastrados.
     *
     * @return Lista de professores.
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    /**
     * Busca um professor pelo ID.
     *
     * @param id ID do professor.
     * @return O professor correspondente, se encontrado.
     */
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    /**
     * Exclui um professor pelo ID.
     *
     * @param id ID do professor a ser excluído.
     */
    public void deleteTeacher(Long id) {
        teacherRepository.deleteById(id);
    }
}
