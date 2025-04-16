package com.dto;

import java.util.List;
import java.util.Map;

/**
 * DTO detalhado para transportar informações completas
 * utilizadas nos relatórios gerais do sistema SELICE.
 */
public class DetailedReportDTO {

    // Estatísticas gerais das faculdades
    private long totalFaculties;
    private long publicFaculties;
    private long privateFaculties;

    // Coordenadores
    private Map<String, Long> coordinatorsByType;
    private Map<String, Long> coordinatorsByCourse;
    private long unassignedCoordinators;

    // Detalhes dos cursos oferecidos pelas faculdades
    private List<CourseDetails> coursesDetails;

    // Estatísticas gerais das escolas
    private long totalSchools;
    private long publicSchools;
    private long privateSchools;
    private List<SchoolDetails> schoolsDetails;

    // Estatísticas gerais consolidadas por faculdade
    private long totalStudents;
    private Map<String, Long> internsStatus;
    private long totalTeachers;
    private long activeCoordinators;
    private Map<String, Long> partnerSchools;

    // Localizações geográficas (para mapa)
    private List<LocationData> locations;

    // Getters e Setters Gerais
    public long getTotalFaculties() { return totalFaculties; }
    public void setTotalFaculties(long totalFaculties) { this.totalFaculties = totalFaculties; }

    public long getPublicFaculties() { return publicFaculties; }
    public void setPublicFaculties(long publicFaculties) { this.publicFaculties = publicFaculties; }

    public long getPrivateFaculties() { return privateFaculties; }
    public void setPrivateFaculties(long privateFaculties) { this.privateFaculties = privateFaculties; }

    public Map<String, Long> getCoordinatorsByType() { return coordinatorsByType; }
    public void setCoordinatorsByType(Map<String, Long> coordinatorsByType) { this.coordinatorsByType = coordinatorsByType; }

    public Map<String, Long> getCoordinatorsByCourse() { return coordinatorsByCourse; }
    public void setCoordinatorsByCourse(Map<String, Long> coordinatorsByCourse) { this.coordinatorsByCourse = coordinatorsByCourse; }

    public long getUnassignedCoordinators() { return unassignedCoordinators; }
    public void setUnassignedCoordinators(long unassignedCoordinators) { this.unassignedCoordinators = unassignedCoordinators; }

    public List<CourseDetails> getCoursesDetails() { return coursesDetails; }
    public void setCoursesDetails(List<CourseDetails> coursesDetails) { this.coursesDetails = coursesDetails; }

    public long getTotalSchools() { return totalSchools; }
    public void setTotalSchools(long totalSchools) { this.totalSchools = totalSchools; }

    public long getPublicSchools() { return publicSchools; }
    public void setPublicSchools(long publicSchools) { this.publicSchools = publicSchools; }

    public long getPrivateSchools() { return privateSchools; }
    public void setPrivateSchools(long privateSchools) { this.privateSchools = privateSchools; }

    public List<SchoolDetails> getSchoolsDetails() { return schoolsDetails; }
    public void setSchoolsDetails(List<SchoolDetails> schoolsDetails) { this.schoolsDetails = schoolsDetails; }

    public long getTotalStudents() { return totalStudents; }
    public void setTotalStudents(long totalStudents) { this.totalStudents = totalStudents; }

    public Map<String, Long> getInternsStatus() { return internsStatus; }
    public void setInternsStatus(Map<String, Long> internsStatus) { this.internsStatus = internsStatus; }

    public long getTotalTeachers() { return totalTeachers; }
    public void setTotalTeachers(long totalTeachers) { this.totalTeachers = totalTeachers; }

    public long getActiveCoordinators() { return activeCoordinators; }
    public void setActiveCoordinators(long activeCoordinators) { this.activeCoordinators = activeCoordinators; }

    public Map<String, Long> getPartnerSchools() { return partnerSchools; }
    public void setPartnerSchools(Map<String, Long> partnerSchools) { this.partnerSchools = partnerSchools; }

    public List<LocationData> getLocations() { return locations; }
    public void setLocations(List<LocationData> locations) { this.locations = locations; }
}
