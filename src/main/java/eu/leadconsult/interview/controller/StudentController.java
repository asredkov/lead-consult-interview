package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.api.StudentApi;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class StudentController implements StudentApi {
    private final StudentService studentService;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        return studentService.createStudent(studentDTO);
    }

    @Override
    public List<StudentDTO> getStudents() {
        return studentService.getStudents();
    }

    @Override
    public StudentDTO getStudent(int id) {
        return studentService.getStudent(id);
    }

    @Override
    public StudentDTO updateStudent(int id, StudentDTO studentDTO) {
        return studentService.updateStudent(id, studentDTO);
    }

    @Override
    public void deleteStudent(int id) {
        studentService.deleteStudent(id);
    }

    @Override
    public long countStudents() {
        return studentService.countStudents();
    }
}
