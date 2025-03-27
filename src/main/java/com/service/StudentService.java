package com.service;

import com.model.Student;
import com.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço responsável pela lógica de negócios dos estudantes.
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Busca todos os estudantes.
     *
     * @return Lista de estudantes
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Busca um estudante pelo ID.
     *
     * @param id ID do estudante
     * @return Optional contendo o estudante ou vazio se não encontrado
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Busca estudantes com horas pendentes <= maxHours.
     *
     * @param maxHours Valor máximo de horas pendentes
     * @return Lista de estudantes
     */
    public List<Student> getStudentsByHours(int maxHours) {
        return studentRepository.findByHoursPendingLessThanEqual(maxHours);
    }

    /**
     * Cria um novo estudante.
     *
     * @param student Estudante a ser criado
     * @return Estudante criado
     */
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Atualiza um estudante existente.
     *
     * @param id      ID do estudante a ser atualizado
     * @param student Dados atualizados do estudante
     * @return Estudante atualizado
     */
    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id).map(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setEmail(student.getEmail());
            existingStudent.setCollege(student.getCollege());
            existingStudent.setTeacher(student.getTeacher());
            existingStudent.setHoursPending(student.getHoursPending());
            existingStudent.setUpdatedAt(student.getUpdatedAt());
            return studentRepository.save(existingStudent);
        }).orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
    }

    /**
     * Exclui um estudante pelo ID.
     *
     * @param id ID do estudante a ser excluído
     */
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
