package com.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * O coordenador é como o "guia" que ajuda a manter as coisas em ordem,
 * seja em uma escola ou em uma faculdade no sistema SELICE.
 */
@Entity
@Table(name = "coordinators") // A tabela no banco será chamada "coordinators".
public class Coordinator {

    @Id // Nosso RG único para cada coordenador!
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco gera o ID automaticamente.
    private Long id;

    @Column(name = "name", nullable = false) // Nome do coordenador. Afinal, todo mundo tem um nome, né?
    @NotBlank(message = "Nome é obrigatório.")
    private String name;

    @Column(name = "email", nullable = false, unique = true) // E-mail do coordenador, que serve como identificação única.
    @Email(message = "E-mail inválido.")
    @NotBlank(message = "E-mail é obrigatório.")
    private String email;

    @Column(name = "phone_number", nullable = true, length = 15) // Telefone para contato.
    private String phoneNumber;

    @Column(name = "department", nullable = true, length = 100) // Departamento ou área de atuação.
    private String department;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true) // Este coordenador pode estar vinculado a uma escola.
    @JsonBackReference(value = "school-coordinator")
    private School school;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = true) // Ou ele pode estar vinculado a uma faculdade.
    @JsonBackReference(value = "college-coordinator")
    private College college;

    // **Construtor padrão**: Uma espécie de "passaporte" para criar o coordenador sem informações extras.
    public Coordinator() {}

    // **Construtor parametrizado**: Para quem já sabe nome, e-mail e onde o coordenador trabalha!
    public Coordinator(String name, String email, String phoneNumber, String department, School school, College college) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.school = school;
        this.college = college;
    }

    // Aqui temos as chaves (setters) e portões (getters) para acessar e modificar os atributos.
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    @Override
    public String toString() {
        // Queremos que as informações sejam fáceis de entender ao exibir o coordenador.
        return "Coordinator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department='" + department + '\'' +
                ", school=" + (school != null ? school.getName() : "Nenhuma") +
                ", college=" + (college != null ? college.getName() : "Nenhuma") +
                '}';
    }
}
