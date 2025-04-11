package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da escola é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotBlank(message = "O endereço é obrigatório.")
    @Size(min = 5, max = 200, message = "O endereço deve ter entre 5 e 200 caracteres.")
    @Column(name = "address", nullable = false)
    private String address;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(min = 2, max = 100, message = "A cidade deve ter entre 2 e 100 caracteres.")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres.")
    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "type")
    private String type;

    @Column(name = "status")
    private String status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "inep_code")
    private String inepCode;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("school")
    private List<Coordinator> coordinators;

    @ManyToMany(mappedBy = "schools")
    @JsonIgnoreProperties("schools") // evita loop no lado do professor
    private List<Teacher> teachers;

    public School() {}

    public School(String name, String address, String city, String state) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInepCode() {
        return inepCode;
    }

    public void setInepCode(String inepCode) {
        this.inepCode = inepCode;
    }

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", inepCode='" + inepCode + '\'' +
                '}';
    }
}
