package com.repository;

//import com.example.SeliceApplication;
//import com.model.College;
//import com.model.School;
//import com.model.Student;
//import com.model.Teacher;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;

//import java.util.List;

//import static org.junit.jupiter.api.Assertions.assertFalse;

//@SpringBootTest(classes = SeliceApplication.class)
//@ActiveProfiles("test")
//public class StudentRepositoryTest {

//    @Autowired
//    private StudentRepository studentRepository;

//    @Autowired
//    private CollegeRepository collegeRepository;

//    @Autowired
//    private TeacherRepository teacherRepository;

//    @Autowired
//    private SchoolRepository schoolRepository;

//    @BeforeEach
//    public void setup() {
//        // Configurações iniciais
//        School school = schoolRepository.findByName("Escola Exemplo")
//                .orElseGet(() -> schoolRepository.save(new School(
//                        "Escola Exemplo", "Rua Principal, 123", "Fortaleza", "CE")));

//        College college = collegeRepository.findByName("College of Science")
//                .orElseGet(() -> collegeRepository.save(new College("College of Science", "Campus A")));

//        Teacher teacher = teacherRepository.findByEmail("prof@example.com")
//                .orElseGet(() -> teacherRepository.save(new Teacher(
//                        "Professor", "prof@example.com", "PhD", school)));

//        studentRepository.findByEmail("student@example.com")
//                .orElseGet(() -> studentRepository.save(new Student(
//                        "Student", "student@example.com", college, teacher, 100, 50, 50)));
//    }

//    @Test
//    public void testFindByHoursPendingLessThanEqual() {
//        List<Student> students = studentRepository.findByHoursPendingLessThanEqual(100);
//        assertFalse(students.isEmpty(), "Nenhum estudante encontrado com horas pendentes <= 100");
//    }
//}
