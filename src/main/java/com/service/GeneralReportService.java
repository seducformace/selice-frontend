package com.service;

import com.dto.GeneralReportDTO;
import com.dto.MapLocationDTO;
import com.dto.SchoolInternshipDTO;
import com.dto.TopIdebSchoolDTO;
import com.model.Faculty;
import com.model.School;
import com.model.Student;
import com.repository.FacultyRepository;
import com.repository.SchoolRepository;
import com.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeneralReportService {

    private final FacultyRepository facultyRepository;
    private final SchoolRepository schoolRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public GeneralReportService(
            FacultyRepository facultyRepository,
            SchoolRepository schoolRepository,
            StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.schoolRepository = schoolRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional(readOnly = true)
    public GeneralReportDTO getGeneralReport() {
        List<Faculty> allFaculties = facultyRepository.findAll();
        List<School> allSchools = schoolRepository.findAll();
        List<Student> allStudents = studentRepository.findAll();

        int totalFaculties = allFaculties.size();
        int publicFaculties = (int) allFaculties.stream()
                .filter(f -> "Pública".equalsIgnoreCase(f.getType()))
                .count();
        int privateFaculties = (int) allFaculties.stream()
                .filter(f -> "Privada".equalsIgnoreCase(f.getType()))
                .count();
        int unassignedCoordinators = (int) allFaculties.stream()
                .filter(f -> f.getCoordinators() == null || f.getCoordinators().isEmpty())
                .count();

        List<TopIdebSchoolDTO> topIdebSchools = allSchools.stream()
                .filter(s -> s.getIdeb() != null)
                .sorted((a, b) -> Double.compare(b.getIdeb(), a.getIdeb()))
                .limit(5)
                .map(s -> new TopIdebSchoolDTO(s.getName(), s.getIdeb()))
                .collect(Collectors.toList());

        List<SchoolInternshipDTO> schoolInternships = allSchools.stream()
                .map(school -> {
                    long count = allStudents.stream()
                            .filter(st -> {
                                try {
                                    return st.getSchool() != null && st.getSchool().getId().equals(school.getId());
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .count();
                    return new SchoolInternshipDTO(school.getName(), (int) count);
                })
                .filter(dto -> dto.getInternCount() > 0)
                .collect(Collectors.toList());

        List<MapLocationDTO> mapLocations = allSchools.stream()
                .filter(s -> s.getLatitude() != null && s.getLongitude() != null)
                .map(s -> new MapLocationDTO(
                        s.getName(),
                        s.getType(),
                        s.getCity(),
                        s.getLatitude(),
                        s.getLongitude()
                ))
                .collect(Collectors.toList());

        return new GeneralReportDTO(
                totalFaculties,
                publicFaculties,
                privateFaculties,
                unassignedCoordinators,
                topIdebSchools,
                schoolInternships,
                mapLocations
        );
    }
}
