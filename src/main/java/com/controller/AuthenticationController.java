package com.controller;

import com.dto.LoginRequest;
import com.dto.LoginResponse;
import com.model.User;
import com.model.Student;
import com.repository.UserRepository;
import com.repository.StudentRepository;
import com.utils.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(
        origins = { "http://localhost:4300", "http://localhost:8081" },
        allowCredentials = "true"
)
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    public AuthenticationController(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            StudentRepository studentRepository
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String rawPassword = loginRequest.getPassword();

        logger.info("🔐 [DEBUG] Tentativa de login para: {}", email);

        // Verifica se é um usuário do sistema
        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            boolean senhaConfere = passwordEncoder().matches(rawPassword, user.getPassword());

            logger.info("🔐 [DEBUG MANUAL] Senha fornecida ({}) confere com hash do banco? {}", rawPassword, senhaConfere);

            if (senhaConfere) {
                String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole());
                return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", token));
            }
        }

        // Verifica se é aluno
        Optional<Student> optionalStudent = studentRepository.findByEmail(email);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            boolean senhaConfere = passwordEncoder().matches(rawPassword, student.getPassword());

            logger.info("🎓 [DEBUG MANUAL] Senha fornecida ({}) confere com hash do aluno? {}", rawPassword, senhaConfere);

            if (senhaConfere) {
                String token = jwtTokenProvider.generateToken(student.getEmail(), "STUDENT");
                return ResponseEntity.ok(new LoginResponse("Login realizado com sucesso!", token));
            }
        }

        logger.warn("⛔ E-mail ou senha inválidos: {}", email);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new LoginResponse("E-mail ou senha inválidos.", null));
    }

    @GetMapping("/debug-user")
    public ResponseEntity<?> debugUserPassword() {
        String email = "admin@teste.seduc.ce.gov.br";
        String rawPassword = "admin123";

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        String hashed = optionalUser.get().getPassword();
        boolean match = passwordEncoder().matches(rawPassword, hashed);

        Map<String, Object> response = new HashMap<>();
        response.put("usuário", email);
        response.put("senhaInformada", rawPassword);
        response.put("senhaHashSalva", hashed);
        response.put("senhaConfere", match);

        return ResponseEntity.ok(response);
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
