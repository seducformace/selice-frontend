package com.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 50)
    private String registration;

    @Column(nullable = false, length = 50)
    private String qualification;

    @Column(nullable = false, length = 100)
    private String discipline;

    // Mapeamento ManyToMany entre professores e escolas
    @ManyToMany
    @JoinTable(
            name = "teacher_schools",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "school_id")
    )
    private List<School> schools;

    @Column(name = "oriented_students", nullable = false)
    private int orientedStudents;

    @Column(name = "students_in_progress", nullable = false)
    private int studentsInProgress;

    public Teacher() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public int getOrientedStudents() {
        return orientedStudents;
    }

    public void setOrientedStudents(int orientedStudents) {
        this.orientedStudents = orientedStudents;
    }

    public int getStudentsInProgress() {
        return studentsInProgress;
    }

    public void setStudentsInProgress(int studentsInProgress) {
        this.studentsInProgress = studentsInProgress;
    }
}
