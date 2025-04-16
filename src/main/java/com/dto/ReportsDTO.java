package com.dto;

import java.util.Map;
import java.util.HashMap;

/**
 * Data Transfer Object (DTO) responsável por transportar dados
 * estatísticos relacionados às entidades do sistema SELICE.
 *
 * Inclui contagens totais, agrupamentos específicos por tipo e status.
 */
public class ReportsDTO {

    // Quantidades totais por entidade
    private long totalFaculties;
    private long totalCoordinators;
    private long totalTeachers;
    private long totalStudents;
    private long totalSchools;

    // Contagens específicas para faculdades
    private long activeFacultiesCount;
    private long inactiveFacultiesCount;
    private Map<String, Long> byType = new HashMap<>();
    private Map<String, Long> byStatus = new HashMap<>();

    public ReportsDTO() {}

    // Getters e Setters

    public long getTotalFaculties() {
        return totalFaculties;
    }

    public void setTotalFaculties(long totalFaculties) {
        this.totalFaculties = totalFaculties;
    }

    public long getTotalCoordinators() {
        return totalCoordinators;
    }

    public void setTotalCoordinators(long totalCoordinators) {
        this.totalCoordinators = totalCoordinators;
    }

    public long getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public long getTotalSchools() {
        return totalSchools;
    }

    public void setTotalSchools(long totalSchools) {
        this.totalSchools = totalSchools;
    }

    public long getActiveFacultiesCount() {
        return activeFacultiesCount;
    }

    public void setActiveFacultiesCount(long activeFacultiesCount) {
        this.activeFacultiesCount = activeFacultiesCount;
    }

    public long getInactiveFacultiesCount() {
        return inactiveFacultiesCount;
    }

    public void setInactiveFacultiesCount(long inactiveFacultiesCount) {
        this.inactiveFacultiesCount = inactiveFacultiesCount;
    }

    public Map<String, Long> getByType() {
        return byType;
    }

    public void setByType(Map<String, Long> byType) {
        this.byType = byType;
    }

    public Map<String, Long> getByStatus() {
        return byStatus;
    }

    public void setByStatus(Map<String, Long> byStatus) {
        this.byStatus = byStatus;
    }
}
