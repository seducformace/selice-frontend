package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Entidade que representa um Coordenador.
 */
@Entity
@Table(name = "coordinators")
public class Coordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório.")
    @Column(nullable = false)
    private String name;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "E-mail é obrigatório.")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "CPF é obrigatório.")
    @Size(min = 11, max = 14, message = "O CPF deve ter entre 11 e 14 caracteres.")
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String department;

    @Column
    private String status = "ATIVO";

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "school_id", foreignKey = @ForeignKey(name = "fk_coordinator_school"), nullable = true)
    @JsonIgnoreProperties({"coordinators", "teachers", "hibernateLazyInitializer", "handler"})
    private School school;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "faculty_id", foreignKey = @ForeignKey(name = "fk_coordinator_faculty"), nullable = true)
    @JsonIgnoreProperties({"coordinators", "students", "hibernateLazyInitializer", "handler"})
    private Faculty faculty;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coordinator_linked_faculties",
            joinColumns = @JoinColumn(name = "coordinator_id"),
            inverseJoinColumns = @JoinColumn(name = "faculty_id")
    )
    @JsonIgnoreProperties({"coordinators", "hibernateLazyInitializer", "handler"})
    private List<Faculty> linkedFaculties;

    public Coordinator() {}

    public Coordinator(String name, String email, String cpf, String phoneNumber, String department, School school, Faculty faculty) {
        this.name = name;
        this.email = email;
        this.cpf = cpf;
        this.phoneNumber = phoneNumber;
        this.department = department;
        this.school = school;
        this.faculty = faculty;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public School getSchool() { return school; }
    public void setSchool(School school) { this.school = school; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public List<Faculty> getLinkedFaculties() { return linkedFaculties; }
    public void setLinkedFaculties(List<Faculty> linkedFaculties) { this.linkedFaculties = linkedFaculties; }

    @Override
    public String toString() {
        return "Coordinator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", department='" + department + '\'' +
                ", status='" + status + '\'' +
                ", school=" + (school != null ? school.getName() : "Nenhuma") +
                ", faculty=" + (faculty != null ? faculty.getName() : "Nenhuma") +
                ", linkedFaculties=" + (linkedFaculties != null ? linkedFaculties.size() : 0) +
                '}';
    }
}
