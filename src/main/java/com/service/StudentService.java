package com.service;

import com.dto.StudentDTO;
import com.model.Student;
import com.model.User;
import com.repository.StudentRepository;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retorna todos os estudantes como entidades completas.
     */
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Retorna todos os estudantes como DTOs com JOIN FETCH.
     */
    public List<StudentDTO> getAllStudentsAsDTO() {
        try {
            return studentRepository.findAllWithRelations().stream()
                    .map(student -> new StudentDTO(
                            student.getId(),
                            student.getName(),
                            student.getCpf(),
                            student.getEmail(),
                            student.getCourse(),
                            (student.getCollege() != null && student.getCollege().getName() != null)
                                    ? student.getCollege().getName() : "Não vinculado",
                            (student.getSchool() != null && student.getSchool().getName() != null)
                                    ? student.getSchool().getName() : "Não vinculado",
                            (student.getTeacher() != null && student.getTeacher().getName() != null)
                                    ? student.getTeacher().getName() : "Não vinculado"
                    ))
                    .toList();
        } catch (Exception e) {
            System.err.println("ERRO AO BUSCAR ESTUDANTES COMO DTO:");
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar lista de estudantes.");
        }
    }

    /**
     * Busca um estudante por ID.
     */
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    /**
     * Retorna estudantes com horas pendentes menores ou iguais ao valor especificado.
     */
    public List<Student> getStudentsByHours(int maxHours) {
        return studentRepository.findByHoursPendingLessThanEqual(maxHours);
    }

    /**
     * Cria e salva um novo estudante.
     */
    public Student createStudent(Student student) {
        LocalDateTime now = LocalDateTime.now();
        student.setCreatedAt(now);
        student.setUpdatedAt(now);
        student.setRegistrationDate(now);

        student.setHoursCompleted(student.getHoursCompleted() == null ? 0 : student.getHoursCompleted());
        student.setHoursPending(student.getHoursPending() == null ? 0 : student.getHoursPending());
        student.setHoursRemaining(student.getHoursRemaining() == null ? 0 : student.getHoursRemaining());

        // Cria usuário no banco com base no e-mail do estudante
        userRepository.findByEmailIgnoreCase(student.getEmail()).ifPresentOrElse(
                u -> {}, // Usuário já existe, não faz nada
                () -> {
                    User user = new User();
                    user.setEmail(student.getEmail());
                    user.setName(student.getName());
                    user.setPassword(passwordEncoder.encode("123456"));
                    user.setRole("STUDENT");
                    userRepository.save(user);
                }
        );

        return studentRepository.save(student);
    }

    /**
     * Atualiza os dados de um estudante existente.
     */
    public Student updateStudent(Long id, Student student) {
        return studentRepository.findById(id)
                .map(existing -> {
                    existing.setName(student.getName());
                    existing.setEmail(student.getEmail());
                    existing.setCpf(student.getCpf());
                    existing.setCourse(student.getCourse());
                    existing.setCollege(student.getCollege());
                    existing.setTeacher(student.getTeacher());
                    existing.setSchool(student.getSchool());
                    existing.setHoursPending(student.getHoursPending() == null ? 0 : student.getHoursPending());
                    existing.setHoursCompleted(student.getHoursCompleted() == null ? 0 : student.getHoursCompleted());
                    existing.setHoursRemaining(student.getHoursRemaining() == null ? 0 : student.getHoursRemaining());
                    existing.setUpdatedAt(LocalDateTime.now());
                    return studentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Estudante não encontrado com ID: " + id));
    }

    /**
     * Remove um estudante pelo ID.
     */
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Estudante com ID " + id + " não encontrado.");
        }
        studentRepository.deleteById(id);
    }
}
