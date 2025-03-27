package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO responsável por transportar a resposta da autenticação.
 * Retorna uma mensagem de status e, se válido, o token JWT.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    // Mensagem para o usuário (ex: sucesso, erro, etc.)
    private String message;

    // Token JWT gerado (pode ser null em caso de falha)
    private String token;

    // Construtor personalizado só com mensagem (útil para erros)
    public LoginResponse(String message) {
        this.message = message;
    }
}
