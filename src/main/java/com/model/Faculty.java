package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "faculties")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "type", nullable = false, length = 20)
    private String type = "PÚBLICA";

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ATIVO";

    @Column(name = "mec_code", length = 20)
    private String mecCode;

    @Column(name = "cnpj", length = 20)
    private String cnpj;

    @Column(name = "dean_name", length = 100)
    private String deanName;

    @Column(name = "partnership_responsible", length = 100)
    private String partnershipResponsible;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @ElementCollection
    @CollectionTable(name = "faculty_courses", joinColumns = @JoinColumn(name = "faculty_id"))
    @Column(name = "course")
    private List<String> offeredCourses;

    @JsonIgnoreProperties("faculty")
    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coordinator> coordinators;

    public Faculty() {}

    public Faculty(String name, String address, String type) {
        this.name = name;
        this.address = address;
        this.type = type;
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
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da faculdade não pode ser vazio.");
        }
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("O endereço da faculdade não pode ser vazio.");
        }
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type == null || (!type.equalsIgnoreCase("PÚBLICA") && !type.equalsIgnoreCase("PRIVADA"))) {
            throw new IllegalArgumentException("O tipo deve ser 'PÚBLICA' ou 'PRIVADA'.");
        }
        this.type = type.toUpperCase();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || (!status.equalsIgnoreCase("ATIVO") && !status.equalsIgnoreCase("INATIVO"))) {
            throw new IllegalArgumentException("O status deve ser 'ATIVO' ou 'INATIVO'.");
        }
        this.status = status.toUpperCase();
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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<String> getOfferedCourses() {
        return offeredCourses;
    }

    public void setOfferedCourses(List<String> offeredCourses) {
        this.offeredCourses = offeredCourses;
    }

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    public void addCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinator.setFaculty(this);
            this.coordinators.add(coordinator);
        }
    }

    public void removeCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinator.setFaculty(null);
            this.coordinators.remove(coordinator);
        }
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", mecCode='" + mecCode + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", deanName='" + deanName + '\'' +
                ", partnershipResponsible='" + partnershipResponsible + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", offeredCourses=" + offeredCourses +
                ", coordinators=" + (coordinators != null ? coordinators.size() : 0) +
                '}';
    }
}
