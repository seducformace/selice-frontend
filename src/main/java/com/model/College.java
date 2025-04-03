package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "colleges")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @JsonIgnoreProperties("college")
    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coordinator> coordinators;

    public College() {}

    public College(String name, String address) {
        this.name = name;
        this.address = address;
    }

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

    public List<Coordinator> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(List<Coordinator> coordinators) {
        this.coordinators = coordinators;
    }

    public void addCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinator.setCollege(this);
            this.coordinators.add(coordinator);
        }
    }

    public void removeCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinator.setCollege(null);
            this.coordinators.remove(coordinator);
        }
    }

    @Override
    public String toString() {
        return "College{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", coordinators=" + (coordinators != null ? coordinators.size() : 0) +
                '}';
    }
}
