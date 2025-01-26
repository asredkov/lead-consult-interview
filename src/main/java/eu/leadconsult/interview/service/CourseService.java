package eu.leadconsult.interview.service;

import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.PersonDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.dto.enums.CourseType;
import eu.leadconsult.interview.entity.Course;

import java.util.List;

public interface CourseService {
    CourseDTO createCourse(CourseDTO courseDTO);

    List<CourseDTO> getCourses();

    CourseDTO getCourse(int id);

    CourseDTO updateCourse(int id, CourseDTO courseDTO);

    void deleteCourse(int id);

    long countByType(CourseType courseType);

    void addStudentToCourse(int courseId, int studentId);

    void addTeacherToCourse(int courseId, int teacherId);

    Course findCourseOrThrow(int id);

    List<PersonDTO> findAllByCourseAndGroup(int courseId, String group);

    List<StudentDTO> getStudentsByCourseAndAge(int courseId, int age);
}
