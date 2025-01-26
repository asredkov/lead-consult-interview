package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.api.CourseApi;
import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.enums.CourseType;
import eu.leadconsult.interview.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CourseController implements CourseApi {

    private final CourseService courseService;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        return courseService.createCourse(courseDTO);
    }

    @Override
    public List<CourseDTO> getCourses() {
        return courseService.getCourses();
    }

    @Override
    public CourseDTO getCourse(int id) {
        return courseService.getCourse(id);
    }

    @Override
    public CourseDTO updateCourse(int id, CourseDTO courseDTO) {
        return courseService.updateCourse(id, courseDTO);
    }

    @Override
    public void deleteCourse(int id) {
        courseService.deleteCourse(id);
    }

    @Override
    public long countByType(CourseType courseType) {
        return courseService.countByType(courseType);
    }

    @Override
    public void addStudentToCourse(int courseId, int studentId) {
        courseService.addStudentToCourse(courseId, studentId);
    }

    @Override
    public void addTeacherToCourse(int courseId, int teacherId) {
        courseService.addTeacherToCourse(courseId, teacherId);
    }
}
