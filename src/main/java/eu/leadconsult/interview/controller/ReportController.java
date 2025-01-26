package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.api.ReportApi;
import eu.leadconsult.interview.dto.PersonDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.entity.Course;
import eu.leadconsult.interview.mapper.StudentMapper;
import eu.leadconsult.interview.service.CourseService;
import eu.leadconsult.interview.service.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ReportController implements ReportApi {
    private final CourseService courseService;
    private final StudentService studentService;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentDTO> getStudentsByCourse(int courseId) {
        Course course = courseService.findCourseOrThrow(courseId);

        return course.getStudents().stream()
                .map(studentMapper::studentToStudentDTO)
                .toList();
    }

    @Override
    public List<StudentDTO> getStudentsByGroup(String group) {
        return studentService.getStudentsByGroup(group);
    }

    @Override
    public List<StudentDTO> getStudentsByCourseAndAge(int courseId, int age) {
        return courseService.getStudentsByCourseAndAge(courseId, age);
    }

    @Override
    public List<PersonDTO> findAllByCourseAndGroup(int courseId, String group) {
        return courseService.findAllByCourseAndGroup(courseId, group);
    }
}
