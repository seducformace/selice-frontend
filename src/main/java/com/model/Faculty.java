package com.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Entidade que representa uma Faculdade no sistema SELICE.
 * Mapeada para a tabela "faculties" no banco de dados.
 */
@Entity
@Table(name = "faculties")
public class Faculty implements Serializable {

    // Identificador único (chave primária) com geração automática
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome da faculdade (único e obrigatório)
    @Column(nullable = false, unique = true)
    private String name;

    // Código do MEC (obrigatório e único)
    @Column(nullable = false, unique = true)
    private String mecCode;

    // CNPJ da instituição (obrigatório e único)
    @Column(nullable = false, unique = true)
    private String cnpj;

    // Nome do reitor da instituição
    @Column(nullable = false)
    private String deanName;

    // Nome do responsável pela parceria com a SEDUC
    @Column(nullable = false)
    private String partnershipResponsible;

    // Telefone de contato
    @Column(nullable = false)
    private String contactPhone;

    // Cidade da faculdade
    @Column(nullable = false)
    private String city;

    // Estado (ex: "Ceará")
    @Column(nullable = false)
    private String state;

    // Lista de cursos oferecidos pela faculdade
    @ElementCollection
    @CollectionTable(
            name = "faculty_courses",
            joinColumns = @JoinColumn(name = "faculty_id")
    )
    @Column(name = "course")
    private List<String> offeredCourses;

    /**
     * Construtor padrão exigido pelo JPA.
     */
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
