package com.service;

import com.model.School;
import com.model.Teacher;
import com.model.User;
import com.repository.SchoolRepository;
import com.repository.TeacherRepository;
import com.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Cria um novo professor, garantindo que esteja vinculado a pelo menos uma escola válida,
     * que CPF e e-mail não estejam duplicados, e cria automaticamente o login.
     */
    public Teacher createTeacher(Teacher teacher) {
        // Validação de e-mail duplicado no repositório de professores e de usuários
        if (teacherRepository.existsByEmail(teacher.getEmail()) || userRepository.findByEmailIgnoreCase(teacher.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Já existe um professor ou usuário com este e-mail.");
        }

        // Validação de CPF duplicado
        if (teacherRepository.existsByCpf(teacher.getCpf())) {
            throw new IllegalArgumentException("Já existe um professor com este CPF.");
        }

        // Validação da vinculação com pelo menos uma escola
        if (teacher.getSchools() == null || teacher.getSchools().isEmpty()) {
            throw new IllegalArgumentException("O professor deve estar vinculado a pelo menos uma escola.");
        }

        // Busca e validação das escolas informadas
        List<School> validSchools = new ArrayList<>();
        for (School school : teacher.getSchools()) {
            School persisted = schoolRepository.findById(school.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Escola com ID " + school.getId() + " não encontrada."));
            validSchools.add(persisted);
        }

        teacher.setSchools(validSchools);
        Teacher savedTeacher = teacherRepository.save(teacher);

        // Criação automática do usuário com senha padrão criptografada
        User user = new User();
        user.setName(teacher.getName());
        user.setEmail(teacher.getEmail());
        user.setPassword(passwordEncoder.encode("123456"));
        user.setRole("TEACHER");

        userRepository.save(user);

        return savedTeacher;
    }

    /**
     * Retorna os dados do professor logado, baseado no token JWT.
     */
    public Teacher getCurrentTeacherByToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        String email = decodedJWT.getClaim("sub").asString(); // "sub" representa o e-mail no JWT

        return teacherRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Professor não encontrado com e-mail: " + email));
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

        // Atualização de escolas vinculadas
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
