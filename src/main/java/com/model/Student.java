package com.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Entidade responsável por mapear os alunos universitários no sistema SELICE.
 */
@Entity
@Table(name = "students")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "college_id")
    @JsonIgnoreProperties({"students", "hibernateLazyInitializer", "handler"})
    private College college;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @JsonIgnoreProperties({"students", "hibernateLazyInitializer", "handler"})
    private Teacher teacher;

    @Column(name = "hours_pending", nullable = false)
    private int hoursPending;

    @Column(name = "hours_completed", nullable = false)
    private int hoursCompleted;

    @Column(name = "hours_remaining", nullable = false)
    private int hoursRemaining;

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor padrão
    public Student() {}

    // Construtor para inicializar dados
    public Student(String name, String email, College college, Teacher teacher, int hoursPending, int hoursCompleted, int hoursRemaining) {
        this.name = name;
        this.email = email;
        this.college = college;
        this.teacher = teacher;
        this.hoursPending = hoursPending;
        this.hoursCompleted = hoursCompleted;
        this.hoursRemaining = hoursRemaining;
        this.registrationDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getHoursPending() {
        return hoursPending;
    }

    public void setHoursPending(int hoursPending) {
        this.hoursPending = hoursPending;
    }

    public int getHoursCompleted() {
        return hoursCompleted;
    }

    public void setHoursCompleted(int hoursCompleted) {
        this.hoursCompleted = hoursCompleted;
    }

    public int getHoursRemaining() {
        return hoursRemaining;
    }

    public void setHoursRemaining(int hoursRemaining) {
        this.hoursRemaining = hoursRemaining;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
