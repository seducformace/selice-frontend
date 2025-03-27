package com.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidade responsável por mapear os professores no sistema SELICE.
 * Professores acompanham alunos universitários e possuem pré-requisitos de formação.
 */
@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "qualification", nullable = false, length = 200)
    private String qualification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> students;

    // Construtor padrão
    public Teacher() {}

    // Construtor parametrizado
    public Teacher(String name, String email, String qualification, School school) {
        if (school == null) {
            throw new IllegalArgumentException("A escola não pode ser nula.");
        }
        this.name = name;
        this.email = email;
        this.qualification = qualification;
        this.school = school;
    }

    // Getters e Setters com validações
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("O e-mail não pode ser vazio.");
        }
        this.email = email;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        if (qualification == null || qualification.trim().isEmpty()) {
            throw new IllegalArgumentException("A qualificação não pode ser vazia.");
        }
        this.qualification = qualification;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        if (school == null) {
            throw new IllegalArgumentException("A escola não pode ser nula.");
        }
        this.school = school;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", qualification='" + qualification + '\'' +
                ", school=" + (school != null ? school.getName() : "None") +
                '}';
    }
}
