package com.model;

import jakarta.persistence.*;

@Entity
@Table(name = "admins", schema = "public") // Define o nome da tabela
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // Adicionado comprimento máximo para username
    private String username;

    @Column(nullable = false, length = 100) // Adicionado comprimento máximo para password
    private String password;

    // Construtor padrão
    public Admin() {}

    // Construtor com parâmetros
    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
