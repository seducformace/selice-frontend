package com.dto;

import java.util.List;

public class GeneralReportDTO {

    private int totalFaculties;
    private int publicFaculties;
    private int privateFaculties;
    private int unassignedCoordinators;

    private List<TopIdebSchoolDTO> topIdebSchools;
    private List<SchoolInternshipDTO> schoolInternships;
    private List<MapLocationDTO> mapLocations;

    public GeneralReportDTO() {
    }

    public GeneralReportDTO(int totalFaculties, int publicFaculties, int privateFaculties,
                            int unassignedCoordinators,
                            List<TopIdebSchoolDTO> topIdebSchools,
                            List<SchoolInternshipDTO> schoolInternships,
                            List<MapLocationDTO> mapLocations) {
        this.totalFaculties = totalFaculties;
        this.publicFaculties = publicFaculties;
        this.privateFaculties = privateFaculties;
        this.unassignedCoordinators = unassignedCoordinators;
        this.topIdebSchools = topIdebSchools;
        this.schoolInternships = schoolInternships;
        this.mapLocations = mapLocations;
    }

    public int getTotalFaculties() {
        return totalFaculties;
    }

    public void setTotalFaculties(int totalFaculties) {
        this.totalFaculties = totalFaculties;
    }

    public int getPublicFaculties() {
        return publicFaculties;
    }

    public void setPublicFaculties(int publicFaculties) {
        this.publicFaculties = publicFaculties;
    }

    public int getPrivateFaculties() {
        return privateFaculties;
    }

    public void setPrivateFaculties(int privateFaculties) {
        this.privateFaculties = privateFaculties;
    }

    public int getUnassignedCoordinators() {
        return unassignedCoordinators;
    }

    public void setUnassignedCoordinators(int unassignedCoordinators) {
        this.unassignedCoordinators = unassignedCoordinators;
    }

    public List<TopIdebSchoolDTO> getTopIdebSchools() {
        return topIdebSchools;
    }

    public void setTopIdebSchools(List<TopIdebSchoolDTO> topIdebSchools) {
        this.topIdebSchools = topIdebSchools;
    }

    public List<SchoolInternshipDTO> getSchoolInternships() {
        return schoolInternships;
    }

    public void setSchoolInternships(List<SchoolInternshipDTO> schoolInternships) {
        this.schoolInternships = schoolInternships;
    }

    public List<MapLocationDTO> getMapLocations() {
        return mapLocations;
    }

    public void setMapLocations(List<MapLocationDTO> mapLocations) {
        this.mapLocations = mapLocations;
    }
}
