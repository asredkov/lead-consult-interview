package eu.leadconsult.interview.service;

import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.entity.Student;

import java.util.List;

public interface StudentService {
    StudentDTO createStudent(StudentDTO studentDTO);

    StudentDTO getStudent(int id);

    List<StudentDTO> getStudents();

    List<StudentDTO> getStudentsByGroup(String group);

    StudentDTO updateStudent(int id, StudentDTO studentDTO);

    void deleteStudent(int id);

    long countStudents();

    Student findStudentOrThrow(int id);
}
