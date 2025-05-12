package com.dto;

/**
 * Data Transfer Object (DTO) para representar dados do estudante.
 */
public class StudentDTO {
    private Long id;
    private String name;
    private String cpf;
    private String email;

    private String course;
    private String collegeName;
    private String schoolName;
    private String teacherName;

    private Long courseId;
    private Long collegeId;
    private Long schoolId;
    private Long teacherId;

    public StudentDTO() {}

    public StudentDTO(
            Long id,
            String name,
            String cpf,
            String email,
            String course,
            String collegeName,
            String schoolName,
            String teacherName,
            Long collegeId,
            Long schoolId,
            Long teacherId,
            Long courseId
    ) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.course = course;
        this.collegeName = collegeName;
        this.schoolName = schoolName;
        this.teacherName = teacherName;
        this.collegeId = collegeId;
        this.schoolId = schoolId;
        this.teacherId = teacherId;
        this.courseId = courseId;
    }

    // Getters e Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }

    public void setCourse(String course) { this.course = course; }

    public String getCollegeName() { return collegeName; }

    public void setCollegeName(String collegeName) { this.collegeName = collegeName; }

    public String getSchoolName() { return schoolName; }

    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }

    public String getTeacherName() { return teacherName; }

    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

    public Long getCourseId() { return courseId; }

    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getCollegeId() { return collegeId; }

    public void setCollegeId(Long collegeId) { this.collegeId = collegeId; }

    public Long getSchoolId() { return schoolId; }

    public void setSchoolId(Long schoolId) { this.schoolId = schoolId; }

    public Long getTeacherId() { return teacherId; }

    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }
}
