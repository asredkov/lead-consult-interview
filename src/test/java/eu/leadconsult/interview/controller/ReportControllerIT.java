package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.PersonDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.dto.enums.PersonType;
import eu.leadconsult.interview.service.CourseService;
import eu.leadconsult.interview.service.StudentService;
import eu.leadconsult.interview.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/courses-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReportControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    StudentService studentService;

    @Autowired
    TeacherService teacherService;

    @Autowired
    CourseService courseService;

    private final RestTemplate restTemplate = new RestTemplate();
    private String reportsBaseUrl() {
        return "http://localhost:" + port + "/api/reports";
    }

    @BeforeEach
    void setup() {
        List<StudentDTO> students = studentService.getStudents();
        List<TeacherDTO> teachers = teacherService.getTeachers();
        List<CourseDTO> courses = courseService.getCourses();

        courseService.addStudentToCourse(courses.get(0).getId(), students.get(0).getId());
        courseService.addStudentToCourse(courses.get(0).getId(), students.get(1).getId());
        courseService.addStudentToCourse(courses.get(0).getId(), students.get(2).getId());
        courseService.addTeacherToCourse(courses.get(0).getId(), teachers.get(0).getId());

        courseService.addStudentToCourse(courses.get(1).getId(), students.get(1).getId());
        courseService.addStudentToCourse(courses.get(1).getId(), students.get(2).getId());
        courseService.addTeacherToCourse(courses.get(1).getId(), teachers.get(0).getId());
        courseService.addTeacherToCourse(courses.get(1).getId(), teachers.get(1).getId());
    }

    @Test
    void studentsByGroup() {
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(reportsBaseUrl() + "/studentsByGroup/JAVA", StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);

        response = restTemplate.getForEntity(reportsBaseUrl() + "/studentsByGroup/PYTHON", StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void studentsByGroupAndAge() {
        List<CourseDTO> courses = courseService.getCourses();

        String studentsByGroupAndAgeUrl = reportsBaseUrl() + "/studentsByCourseAndAge";
        String urlWithParams = UriComponentsBuilder.fromUriString(studentsByGroupAndAgeUrl)
                .queryParam("courseId", courses.get(0).getId())
                .queryParam("age", 20)
                .toUriString();

        ResponseEntity<StudentDTO[]> response =
                restTemplate.getForEntity(urlWithParams, StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);

        urlWithParams = UriComponentsBuilder.fromUriString(studentsByGroupAndAgeUrl)
                .queryParam("courseId", courses.get(0).getId())
                .queryParam("age", 25)
                .toUriString();
        response = restTemplate.getForEntity(urlWithParams, StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().length);

        urlWithParams = UriComponentsBuilder.fromUriString(studentsByGroupAndAgeUrl)
                .queryParam("courseId", courses.get(1).getId())
                .queryParam("age", 20)
                .toUriString();
        response = restTemplate.getForEntity(urlWithParams, StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }

    @Test
    void studentsByCourse() {
        List<CourseDTO> courses = courseService.getCourses();

        String studentsByCourseUrl = reportsBaseUrl() + "/studentsByCourse/" + courses.get(0).getId();
        ResponseEntity<StudentDTO[]> response = restTemplate.getForEntity(studentsByCourseUrl, StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length);

        studentsByCourseUrl = reportsBaseUrl() + "/studentsByCourse/" + courses.get(1).getId();
        response = restTemplate.getForEntity(studentsByCourseUrl, StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);
    }

    @Test
    void findAllByCourseAndGroup() {
        List<StudentDTO> students = studentService.getStudents();
        List<TeacherDTO> teachers = teacherService.getTeachers();
        List<CourseDTO> courses = courseService.getCourses();

        String findAllByCourseAndGroupUrl = reportsBaseUrl() + "/findAllByCourseAndGroup";
        String urlWithParams = UriComponentsBuilder.fromUriString(findAllByCourseAndGroupUrl)
                .queryParam("courseId", courses.get(0).getId())
                .queryParam("group", "JAVA")
                .toUriString();
        ResponseEntity<PersonDTO[]> response = restTemplate.getForEntity(urlWithParams, PersonDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length);
        assertTrue(Arrays.stream(response.getBody())
                .anyMatch(person ->
                    person.getType().equals(PersonType.TEACHER) &&
                    person.getId() == teachers.get(0).getId() &&
                    person.getName().equals(teachers.get(0).getName()) &&
                    person.getGroup().equals(teachers.get(0).getGroup())
                ));

        urlWithParams = UriComponentsBuilder.fromUriString(findAllByCourseAndGroupUrl)
                .queryParam("courseId", courses.get(1).getId())
                .queryParam("group", "PYTHON")
                .toUriString();
        response = restTemplate.getForEntity(urlWithParams, PersonDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
        assertTrue(Arrays.stream(response.getBody())
                .anyMatch(person ->
                        person.getType().equals(PersonType.STUDENT) &&
                        person.getId() == students.get(2).getId() &&
                        person.getName().equals(students.get(2).getName()) &&
                        person.getGroup().equals(students.get(2).getGroup())
                ));
    }
}
