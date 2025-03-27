package com.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manipulador global de exceções.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de validação (ex.: campos inválidos).
     *
     * @param ex Exceção gerada por argumentos inválidos.
     * @return ResponseEntity com mensagens de erro detalhadas no formato esperado.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Obtém o primeiro erro de validação
        FieldError firstError = ex.getBindingResult().getFieldErrors().get(0);
        // Cria a resposta com o campo e mensagem de erro
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(firstError.getField(), firstError.getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Trata IllegalArgumentException de forma genérica.
     *
     * @param ex Exceção do tipo IllegalArgumentException.
     * @return ResponseEntity com mensagem de erro.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + ex.getMessage());
    }

    /**
     * Trata exceções genéricas não previstas.
     *
     * @param ex Exceção genérica.
     * @return ResponseEntity com mensagem de erro genérica.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Log detalhado para depuração
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado. Por favor, tente novamente.");
    }
}
