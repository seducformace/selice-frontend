package com.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * Entidade que representa uma Faculdade no sistema SELICE.
 * Mapeada para a tabela "faculties" no banco de dados.
 */
@Entity
@Table(name = "faculties")
public class Faculty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String mecCode;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String deanName;

    @Column(nullable = false)
    private String partnershipResponsible;

    @Column(nullable = false)
    private String contactPhone;

    @Email(message = "E-mail inválido") // ✅ Validação de formato
    @NotBlank(message = "O campo e-mail é obrigatório")
    @Column(nullable = false)
    private String email; // ✅ Campo novo

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @ElementCollection
    @CollectionTable(
            name = "faculty_courses",
            joinColumns = @JoinColumn(name = "faculty_id")
    )
    @Column(name = "course")
    private List<String> offeredCourses;

    public Faculty() {}

    // Getters e Setters

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

    public String getMecCode() {
        return mecCode;
    }

    public void setMecCode(String mecCode) {
        this.mecCode = mecCode;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDeanName() {
        return deanName;
    }

    public void setDeanName(String deanName) {
        this.deanName = deanName;
    }

    public String getPartnershipResponsible() {
        return partnershipResponsible;
    }

    public void setPartnershipResponsible(String partnershipResponsible) {
        this.partnershipResponsible = partnershipResponsible;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<String> getOfferedCourses() {
        return offeredCourses;
    }

    public void setOfferedCourses(List<String> offeredCourses) {
        this.offeredCourses = offeredCourses;
    }
}
