package com.dto;

import java.util.Map;

public class SchoolDetails {

    private String schoolName;
    private String coordinatorName;
    private long totalTeachers;
    private long internsInProgress;
    private long internsCompleted;
    private long internsFailed;
    private long totalStudentsByCourse;

    // Campo adicional opcional (não usado no JPQL)
    private Map<String, Long> teachersByCourse;

    // ✅ Construtor usado pela JPQL
    public SchoolDetails(String schoolName, String coordinatorName, long totalTeachers,
                         long internsInProgress, long internsCompleted, long internsFailed, long totalStudentsByCourse) {
        this.schoolName = schoolName;
        this.coordinatorName = coordinatorName;
        this.totalTeachers = totalTeachers;
        this.internsInProgress = internsInProgress;
        this.internsCompleted = internsCompleted;
        this.internsFailed = internsFailed;
        this.totalStudentsByCourse = totalStudentsByCourse;
    }

    // ✅ Getters e Setters
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public long getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public long getInternsInProgress() {
        return internsInProgress;
    }

    public void setInternsInProgress(long internsInProgress) {
        this.internsInProgress = internsInProgress;
    }

    public long getInternsCompleted() {
        return internsCompleted;
    }

    public void setInternsCompleted(long internsCompleted) {
        this.internsCompleted = internsCompleted;
    }

    public long getInternsFailed() {
        return internsFailed;
    }

    public void setInternsFailed(long internsFailed) {
        this.internsFailed = internsFailed;
    }

    public long getTotalStudentsByCourse() {
        return totalStudentsByCourse;
    }

    public void setTotalStudentsByCourse(long totalStudentsByCourse) {
        this.totalStudentsByCourse = totalStudentsByCourse;
    }

    public Map<String, Long> getTeachersByCourse() {
        return teachersByCourse;
    }

    public void setTeachersByCourse(Map<String, Long> teachersByCourse) {
        this.teachersByCourse = teachersByCourse;
    }
}
