package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador temporário para testar hashes de senha.
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Endpoint para testar se uma senha corresponde ao hash.
     *
     * @param rawPassword    Senha em texto puro.
     * @param hashedPassword Hash armazenado no banco.
     * @return Mensagem indicando se a senha é válida ou inválida.
     */
    @GetMapping("/hash")
    public ResponseEntity<String> testPasswordHash(@RequestParam String rawPassword, @RequestParam String hashedPassword) {
        boolean matches = passwordEncoder.matches(rawPassword, hashedPassword);
        if (matches) {
            return ResponseEntity.ok("Senha válida: o hash corresponde.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha inválida: o hash não corresponde.");
        }
    }
}
