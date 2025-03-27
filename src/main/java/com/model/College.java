package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

/**
 * Esta classe é como a "ficha de cadastro" das faculdades no sistema SELICE!
 * Aqui guardamos o nome, endereço e a equipe de coordenadores de cada faculdade.
 */
@Entity // Dizemos ao JPA: "Olha, isso aqui é uma tabela do banco de dados, beleza?"
@Table(name = "colleges") // O nome da tabela será "colleges".
public class College {

    @Id // Este é o RG único da faculdade!
    @GeneratedValue(strategy = GenerationType.IDENTITY) // E o banco de dados se encarrega de gerar automaticamente.
    private Long id; // Aqui está o número de identificação único.

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name; // Nome da faculdade, que precisa ser especial e único (não aceitamos clones aqui!).

    @Column(name = "address", nullable = false, length = 200)
    private String address; // O endereço da faculdade, para sabermos onde ela mora.

    @JsonIgnoreProperties("college") // Evita recursão infinita ao serializar coordenadores.
    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coordinator> coordinators; // Aqui está o time de coordenadores. Cada um deles tem sua história com a faculdade.

    // **Construtor padrão**: um passe livre para criar faculdades sem perguntas.
    public College() {}

    // **Construtor com parâmetros**: para criar uma faculdade com nome e endereço prontinhos!
    public College(String name, String address) {
        this.name = name;
        this.address = address;
    }

    // A partir daqui, temos os "portões" (getters) e as "chaves" (setters) de cada atributo.
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

    // **Adiciona Coordenador**: mais um membro entra para o time!
    public void addCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinator.setCollege(this); // Ligamos o coordenador à faculdade.
            this.coordinators.add(coordinator);
        }
    }

    // **Remove Coordenador**: despedidas são sempre difíceis...
    public void removeCoordinator(Coordinator coordinator) {
        if (coordinator != null) {
            coordinator.setCollege(null); // Soltamos o vínculo.
            this.coordinators.remove(coordinator);
        }
    }

    @Override
    public String toString() {
        // Vamos mostrar de forma amigável as informações da faculdade!
        return "College{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", coordinators=" + (coordinators != null ? coordinators.size() : 0) +
                '}';
    }
}
