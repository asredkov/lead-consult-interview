package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.dto.enums.CourseType;
import eu.leadconsult.interview.controller.util.MockUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CourseControllerIT {
    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/courses";
    }

    private final int INVALID_ID = 100;
    private final String TITLE = "OOP";
    private final String DESCRIPTION = "Object oriented programming.";
    private final CourseType TYPE = CourseType.MAIN;
    private final CourseDTO mockedCourseDTO = MockUtil.mockCourseDTO(INVALID_ID, TITLE, DESCRIPTION, TYPE);

    @Test
    void createAndGetCourse() {
        ResponseEntity<CourseDTO> response =
                restTemplate.postForEntity(getBaseUrl(), mockedCourseDTO, CourseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        int actualId = response.getBody().getId();
        assertNotEquals(INVALID_ID, response.getBody().getId());
        assertEquals(TITLE, response.getBody().getTitle());
        assertEquals(DESCRIPTION, response.getBody().getDescription());
        assertEquals(TYPE, response.getBody().getType());

        response = restTemplate.getForEntity(getBaseUrl() + "/" + actualId, CourseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(actualId, response.getBody().getId());
        assertEquals(TITLE, response.getBody().getTitle());
        assertEquals(DESCRIPTION, response.getBody().getDescription());
        assertEquals(TYPE, response.getBody().getType());
    }

    @Test
    @Sql(scripts = {"/courses-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getStudents() {
        ResponseEntity<CourseDTO[]> response =
                restTemplate.getForEntity(getBaseUrl(), CourseDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().length);

        CourseDTO[] courses = response.getBody();
        assertEquals("Java Basics", courses[0].getTitle());
        assertEquals("Introduction into JAVA basics.", courses[0].getDescription());
        assertEquals(CourseType.MAIN, courses[0].getType());

        assertEquals("C# Basics", courses[1].getTitle());
        assertEquals("Introduction into C# basics.", courses[1].getDescription());
        assertEquals(CourseType.MAIN, courses[1].getType());

        assertEquals("Data Structure and Algorithms", courses[2].getTitle());
        assertEquals("Introduction to Data Structures and Algorithms.", courses[2].getDescription());
        assertEquals(CourseType.MAIN, courses[2].getType());

        assertEquals("Design Patterns", courses[3].getTitle());
        assertEquals("Gangs of Four Design Patterns.", courses[3].getDescription());
        assertEquals(CourseType.SECONDARY, courses[3].getType());
    }

    @Test
    void getCourseThrowsException() {
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.getForEntity(getBaseUrl() + "/INVALID_ID", CourseDTO.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        String expectedMessage =
                "{\"status\":400,\"message\":\"Method parameter 'id': " +
                "Failed to convert value of type 'java.lang.String' to required type 'int'; " +
                "For input string: \\\"INVALID_ID\\\"\"}";
        assertEquals(expectedMessage, exception.getResponseBodyAsString());
    }

    @Test
    void updateCourse() {
        CourseDTO courseDTO = MockUtil.mockCourseDTO(INVALID_ID, TITLE, DESCRIPTION, TYPE);
        ResponseEntity<CourseDTO> response =
                restTemplate.postForEntity(getBaseUrl(), courseDTO, CourseDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TITLE, response.getBody().getTitle());

        int id = response.getBody().getId();
        String newTitle = "OOP Basics";

        courseDTO.setTitle(newTitle);

        response = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(courseDTO),
                CourseDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(newTitle, response.getBody().getTitle());
    }


    @Test
    void deleteCourse() {
        ResponseEntity<CourseDTO> createResponse =
                restTemplate.postForEntity(getBaseUrl(), mockedCourseDTO, CourseDTO.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        int id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restTemplate.exchange(
                        getBaseUrl() + "/" + id,
                        HttpMethod.DELETE,
                        null,
                        Void.class
                )
        );
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        String expectedMessage =
                "{\"status\":404,\"message\":\"Course with id = " + id + " not found.\"}";
        assertEquals(expectedMessage, exception.getResponseBodyAsString());
    }

    @Test
    @Sql(scripts = {"/courses-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void countCoursesByTypes() {
        ResponseEntity<Long> response = restTemplate.getForEntity(getBaseUrl() + "/countByType/MAIN", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody());

        response = restTemplate.getForEntity(getBaseUrl() + "/countByType/SECONDARY", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody());
    }

    @Test
    void addStudentToCourse() {
        ResponseEntity<CourseDTO> courseResponse =
                restTemplate.postForEntity(getBaseUrl(), mockedCourseDTO, CourseDTO.class);
        assertEquals(HttpStatus.OK, courseResponse.getStatusCode());
        assertNotNull(courseResponse.getBody());
        int courseId = courseResponse.getBody().getId();

        StudentDTO student = MockUtil.mockStudentDTO(-123, "Hristo Peev", 27, "PYTHON");
        ResponseEntity<StudentDTO> studentResponse =
                restTemplate.postForEntity("http://localhost:" + port + "/api/students", student, StudentDTO.class);
        assertEquals(HttpStatus.OK, studentResponse.getStatusCode());
        assertNotNull(studentResponse.getBody());
        int studentId = studentResponse.getBody().getId();

        String addStudentToCourseUrl = getBaseUrl() + "/" + courseId + "/student/" + studentId;
        ResponseEntity<Void> addStudentResponse = restTemplate.postForEntity(addStudentToCourseUrl, null, Void.class);

        assertEquals(HttpStatus.OK, addStudentResponse.getStatusCode());

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.Conflict.class,
                () -> restTemplate.postForEntity(addStudentToCourseUrl, null, Void.class)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    public void addTeacherToCourse() {
        ResponseEntity<CourseDTO> courseResponse =
                restTemplate.postForEntity(getBaseUrl(), mockedCourseDTO, CourseDTO.class);
        assertEquals(HttpStatus.OK, courseResponse.getStatusCode());
        assertNotNull(courseResponse.getBody());
        int courseId = courseResponse.getBody().getId();

        TeacherDTO teacher = MockUtil.mockTeacherDTO(-123, "Stefan Mitov", 37, "PYTHON");
        ResponseEntity<TeacherDTO> teacherResponse =
                restTemplate.postForEntity("http://localhost:" + port + "/api/teachers", teacher, TeacherDTO.class);
        assertEquals(HttpStatus.OK, teacherResponse.getStatusCode());
        assertNotNull(teacherResponse.getBody());
        int teacherId = teacherResponse.getBody().getId();

        String addTeacherToCourseUrl = getBaseUrl() + "/" + courseId + "/teacher/" + teacherId;
        ResponseEntity<Void> addStudentResponse = restTemplate.postForEntity(addTeacherToCourseUrl, null, Void.class);

        assertEquals(HttpStatus.OK, addStudentResponse.getStatusCode());

        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.Conflict.class,
                () -> restTemplate.postForEntity(addTeacherToCourseUrl, null, Void.class)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }
}
