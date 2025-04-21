package com.dto;

public class TopIdebSchoolDTO {
    private String name;
    private double ideb;

    public TopIdebSchoolDTO() {
    }

    public TopIdebSchoolDTO(String name, double ideb) {
        this.name = name;
        this.ideb = ideb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getIdeb() {
        return ideb;
    }

    public void setIdeb(double ideb) {
        this.ideb = ideb;
    }
}
