package com.model;

import jakarta.persistence.*;

/**
 * Entidade que representa os usuários do sistema SELICE.
 * Utilizada para autenticação e controle de acesso via JWT.
 */
@Entity
@Table(name = "users")
public class User {

    // Identificador único do usuário
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do usuário (obrigatório)
    @Column(name = "name", nullable = false)
    private String name;

    // E-mail (único, usado como login)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Senha criptografada
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * Papel do usuário no sistema.
     * Ex: ROLE_ADMIN, ROLE_USER, ROLE_COORDINATOR
     */
    @Column(name = "role", nullable = false)
    private String role;

    // Construtor padrão (obrigatório para JPA)
    public User() {}

    // Construtor completo
    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
