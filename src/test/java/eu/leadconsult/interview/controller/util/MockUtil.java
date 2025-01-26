package eu.leadconsult.interview.controller.util;

import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.dto.enums.CourseType;

public class MockUtil {

    public static StudentDTO mockStudentDTO(int id, String name, int age, String group) {
        StudentDTO student = new StudentDTO();
        student.setId(id);
        student.setName(name);
        student.setAge(age);
        student.setGroup(group);

        return student;
    }

    public static TeacherDTO mockTeacherDTO(int id, String name, int age, String group) {
        TeacherDTO teacher = new TeacherDTO();
        teacher.setId(id);
        teacher.setName(name);
        teacher.setAge(age);
        teacher.setGroup(group);

        return teacher;
    }

    public static CourseDTO mockCourseDTO(int id, String title, String description, CourseType type) {
        CourseDTO course = new CourseDTO();
        course.setId(id);
        course.setTitle(title);
        course.setDescription(description);
        course.setType(type);

        return course;
    }
}
