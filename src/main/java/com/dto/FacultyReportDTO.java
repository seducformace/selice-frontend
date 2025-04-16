package com.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO para consolidar os dados de relatórios por Faculdade.
 * Inclui coordenador responsável, cursos, alunos por curso,
 * total de estagiários e dados de localização para o mapa.
 */
public class FacultyReportDTO {

    private Long facultyId;
    private String facultyName;
    private String city;
    private String state;

    private String coordinatorName;
    private String coordinatorEmail;

    // Lista de cursos oferecidos
    private List<String> courses;

    // Mapa: curso -> quantidade de alunos vinculados
    private Map<String, Long> studentsPerCourse;

    // Mapa: curso -> quantidade total de estagiários
    private Map<String, Long> internsPerCourse;

    // Total de estagiários da faculdade
    private Long totalInterns;

    // Construtor padrão
    public FacultyReportDTO() {}

    // Getters e Setters
    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCoordinatorEmail() {
        return coordinatorEmail;
    }

    public void setCoordinatorEmail(String coordinatorEmail) {
        this.coordinatorEmail = coordinatorEmail;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public Map<String, Long> getStudentsPerCourse() {
        return studentsPerCourse;
    }

    public void setStudentsPerCourse(Map<String, Long> studentsPerCourse) {
        this.studentsPerCourse = studentsPerCourse;
    }

    public Map<String, Long> getInternsPerCourse() {
        return internsPerCourse;
    }

    public void setInternsPerCourse(Map<String, Long> internsPerCourse) {
        this.internsPerCourse = internsPerCourse;
    }

    public Long getTotalInterns() {
        return totalInterns;
    }

    public void setTotalInterns(Long totalInterns) {
        this.totalInterns = totalInterns;
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
}
