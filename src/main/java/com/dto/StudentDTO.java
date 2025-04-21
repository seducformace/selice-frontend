package com.dto;

public class StudentDTO {

    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String course;

    private String collegeName;  // <- nome correto
    private String schoolName;
    private String teacherName;

    public StudentDTO() {}

    public StudentDTO(Long id, String name, String cpf, String email, String course,
                      String collegeName, String schoolName, String teacherName) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.course = course;
        this.collegeName = collegeName;
        this.schoolName = schoolName;
        this.teacherName = teacherName;
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
