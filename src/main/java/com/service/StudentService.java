package com.service;

import com.model.Student;
import com.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
     * Cria um novo estudante com todos os campos obrigatórios preenchidos.
     *
     * @param student Estudante a ser criado
     * @return Estudante criado
     */
    public Student createStudent(Student student) {

        // Definição de datas obrigatórias
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        student.setRegistrationDate(LocalDateTime.now());

        // Garante que campos inteiros obrigatórios nunca sejam nulos
        if (student.getHoursCompleted() == 0) student.setHoursCompleted(0);
        if (student.getHoursPending() == 0) student.setHoursPending(0);
        if (student.getHoursRemaining() == 0) student.setHoursRemaining(0);

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
            existingStudent.setCpf(student.getCpf());
            existingStudent.setCourse(student.getCourse());
            existingStudent.setCollege(student.getCollege());
            existingStudent.setTeacher(student.getTeacher());
            existingStudent.setSchool(student.getSchool());
            existingStudent.setHoursPending(student.getHoursPending());
            existingStudent.setHoursCompleted(student.getHoursCompleted());
            existingStudent.setHoursRemaining(student.getHoursRemaining());
            existingStudent.setUpdatedAt(LocalDateTime.now());
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
