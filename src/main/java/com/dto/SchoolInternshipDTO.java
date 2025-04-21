package com.dto;

public class SchoolInternshipDTO {
    private String schoolName;
    private int internCount;

    public SchoolInternshipDTO() {
    }

    public SchoolInternshipDTO(String schoolName, int internCount) {
        this.schoolName = schoolName;
        this.internCount = internCount;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int getInternCount() {
        return internCount;
    }

    public void setInternCount(int internCount) {
        this.internCount = internCount;
    }
}
