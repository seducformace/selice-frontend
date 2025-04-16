package com.service;

import com.dto.CourseDetails;
import com.dto.LocationData;
import com.dto.ReportsDTO;
import com.dto.DetailedReportDTO;
import com.projection.CoordinatorCourseCount;
import com.repository.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportsService {

    private final FacultyRepository facultyRepository;
    private final CoordinatorRepository coordinatorRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    public ReportsService(
            FacultyRepository facultyRepository,
            CoordinatorRepository coordinatorRepository,
            TeacherRepository teacherRepository,
            StudentRepository studentRepository,
            SchoolRepository schoolRepository
    ) {
        this.facultyRepository = facultyRepository;
        this.coordinatorRepository = coordinatorRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
    }

    public ReportsDTO getFacultiesStats() {
        ReportsDTO dto = new ReportsDTO();

        // Ajustado: nome correto do campo é state
        dto.setActiveFacultiesCount(facultyRepository.countByState("ATIVO"));
        dto.setInactiveFacultiesCount(facultyRepository.countByState("INATIVO"));

        Map<String, Long> byType = new HashMap<>();
        byType.put("PÚBLICA", facultyRepository.countByType("PÚBLICA"));
        byType.put("PRIVADA", facultyRepository.countByType("PRIVADA"));
        dto.setByType(byType);

        Map<String, Long> byStatus = new HashMap<>();
        byStatus.put("ATIVO", facultyRepository.countByState("ATIVO"));
        byStatus.put("INATIVO", facultyRepository.countByState("INATIVO"));
        dto.setByStatus(byStatus);

        return dto;
    }

    public ReportsDTO getDashboardStats() {
        ReportsDTO dto = new ReportsDTO();

        dto.setTotalFaculties(facultyRepository.count());
        dto.setTotalCoordinators(coordinatorRepository.count());
        dto.setTotalTeachers(teacherRepository.count());
        dto.setTotalStudents(studentRepository.count());
        dto.setTotalSchools(schoolRepository.count());

        return dto;
    }

    public DetailedReportDTO getDetailedFacultyReport() {
        DetailedReportDTO report = new DetailedReportDTO();

        report.setTotalFaculties(facultyRepository.count());
        report.setPublicFaculties(facultyRepository.countByType("PÚBLICA"));
        report.setPrivateFaculties(facultyRepository.countByType("PRIVADA"));

        // Conversão manual de List<Object[]> para Map<String, Long>
        List<Object[]> coordinatorData = facultyRepository.getCoordinatorCountsByType();
        Map<String, Long> coordinatorByTypeMap = new HashMap<>();
        for (Object[] row : coordinatorData) {
            if (row[0] != null && row[1] != null) {
                coordinatorByTypeMap.put((String) row[0], (Long) row[1]);
            }
        }
        report.setCoordinatorsByType(coordinatorByTypeMap);

        // Conversão de projeção para Map<String, Long>
        Map<String, Long> coordinatorByCourseMap = new HashMap<>();
        for (CoordinatorCourseCount entry : coordinatorRepository.getCoordinatorCountsByCourse()) {
            coordinatorByCourseMap.put(entry.getCourse(), entry.getTotal());
        }
        report.setCoordinatorsByCourse(coordinatorByCourseMap);

        // Comentado até que getCoursesWithStats() esteja implementado corretamente
        // List<CourseDetails> courseStats = facultyRepository.getCoursesWithStats();
        // report.setCoursesDetails(courseStats);

        return report;
    }

    public DetailedReportDTO getDetailedSchoolsReport() {
        DetailedReportDTO report = new DetailedReportDTO();

        report.setTotalSchools(schoolRepository.count());
        report.setPublicSchools(schoolRepository.countByType("PÚBLICA"));
        report.setPrivateSchools(schoolRepository.countByType("PRIVADA"));

        report.setSchoolsDetails(schoolRepository.getSchoolsDetailedStats());

        return report;
    }

    public List<DetailedReportDTO> getGeneralFacultyStatistics() {
        return facultyRepository.getGeneralFacultyStatistics();
    }

    public List<LocationData> getLocationsForMap() {
        return facultyRepository.getAllLocationsForMap();
    }
}
