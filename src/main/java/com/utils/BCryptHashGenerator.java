package com.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptHashGenerator  {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        System.out.println("Senha: " + rawPassword);
        System.out.println("Hash: " + encodedPassword);
    }
}

