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
     * Retorna todos os estudantes cadastrados.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Retorna um estudante pelo ID.
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Retorna estudantes com horas pendentes abaixo ou igual ao valor informado.
     */
    public List<Student> getStudentsByHours(int maxHours) {
        return studentRepository.findByHoursPendingLessThanEqual(maxHours);
    }

    /**
     * Cria um novo estudante, preenchendo automaticamente datas e valores padrão.
     */
    public Student createStudent(Student student) {
        LocalDateTime now = LocalDateTime.now();
        student.setCreatedAt(now);
        student.setUpdatedAt(now);
        student.setRegistrationDate(now);

        // Garante que campos inteiros obrigatórios não fiquem nulos
        if (student.getHoursCompleted() == 0) student.setHoursCompleted(0);
        if (student.getHoursPending() == 0) student.setHoursPending(0);
        if (student.getHoursRemaining() == 0) student.setHoursRemaining(0);

        return studentRepository.save(student);
    }

    /**
     * Atualiza um estudante existente.
     */
    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id).map(existing -> {
            existing.setName(student.getName());
            existing.setEmail(student.getEmail());
            existing.setCpf(student.getCpf());
            existing.setCourse(student.getCourse());
            existing.setFaculty(student.getFaculty()); // Atualizado para Faculty
            existing.setTeacher(student.getTeacher());
            existing.setSchool(student.getSchool());
            existing.setHoursPending(student.getHoursPending());
            existing.setHoursCompleted(student.getHoursCompleted());
            existing.setHoursRemaining(student.getHoursRemaining());
            existing.setUpdatedAt(LocalDateTime.now());

            return studentRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
    }

    /**
     * Exclui um estudante pelo ID.
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante com ID " + id + " não encontrado.");
        }
        studentRepository.deleteById(id);
    }
}
