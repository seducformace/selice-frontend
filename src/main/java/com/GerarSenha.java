package com;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarSenha {
    public static void main(String[] args) {
        String senha = "123456";
        String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);
        System.out.println("Senha criptografada: " + senhaCriptografada);
    }
}

