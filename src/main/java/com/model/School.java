package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Representa uma Escola no sistema SELICE, contendo informações como nome, endereço, cidade e estado.
 */
@Entity
@Table(name = "schools") // Define a tabela "schools" no banco de dados.
public class School {

    @Id // Define o identificador único.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente pelo banco.
    private Long id;

    @NotBlank(message = "O nome da escola é obrigatório.") // Validação: não permite valores nulos ou vazios.
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.") // Restrições de tamanho.
    @Column(name = "name", nullable = false, unique = true) // Nome único no banco de dados.
    private String name;

    @NotBlank(message = "O endereço é obrigatório.") // Validação para evitar endereços vazios.
    @Size(min = 5, max = 200, message = "O endereço deve ter entre 5 e 200 caracteres.") // Limitação de tamanho.
    @Column(name = "address", nullable = false) // Campo obrigatório.
    private String address;

    @NotBlank(message = "A cidade é obrigatória.") // Validação para evitar cidades vazias.
    @Size(min = 2, max = 100, message = "A cidade deve ter entre 2 e 100 caracteres.") // Restrições de tamanho.
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "O estado é obrigatório.") // Validação: não permite valores nulos ou vazios.
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres.") // Restrições para estados brasileiros.
    @Column(name = "state", nullable = false)
    private String state;

    @JsonIgnore // Ignora esta propriedade na serialização JSON para evitar referências circulares.
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coordinator> coordinators;

    // Construtor padrão necessário para o JPA.
    public School() {}

    // Construtor completo para inicializar a entidade.
    public School(String name, String address, String city, String state) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    // Getters e Setters.
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
