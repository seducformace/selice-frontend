package com.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO responsável por transportar os dados de login da requisição.
 * Utilizado no endpoint POST /api/authentication/login.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    // E-mail do usuário (obrigatório e com validação de formato)
    @NotBlank(message = "O email não pode estar vazio")
    @Email(message = "Formato de email inválido")
    private String email;

    // Senha do usuário (obrigatória)
    @NotBlank(message = "A senha não pode estar vazia")
    private String password;
}
