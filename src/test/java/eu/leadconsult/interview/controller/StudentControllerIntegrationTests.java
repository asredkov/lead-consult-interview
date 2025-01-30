package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.dto.StudentDTO;
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
public class StudentControllerIntegrationTests {
    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/students";
    }

    private final int INVALID_ID = 100;
    private final String NAME = "Ivan Ivanov";
    private final int AGE = 25;
    private final String GROUP = "SQL";
    private final StudentDTO mockedStudentDTO = MockUtil.mockStudentDTO(INVALID_ID, NAME, AGE, GROUP);

    @Test
    void createAndGetStudent() {

        ResponseEntity<StudentDTO> response =
                restTemplate.postForEntity(getBaseUrl(), mockedStudentDTO, StudentDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        int actualId = response.getBody().getId();
        assertNotEquals(INVALID_ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(AGE, response.getBody().getAge());
        assertEquals(GROUP, response.getBody().getGroup());

        response = restTemplate.getForEntity(getBaseUrl() + "/" + actualId, StudentDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(actualId, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(AGE, response.getBody().getAge());
        assertEquals(GROUP, response.getBody().getGroup());
    }

    @Test
    @Sql(scripts = {"/students-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getStudents() {
        ResponseEntity<StudentDTO[]> response =
                restTemplate.getForEntity(getBaseUrl(), StudentDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().length);

        StudentDTO[] students = response.getBody();
        assertEquals("Ivan Ivanov", students[0].getName());
        assertEquals(22, students[0].getAge());
        assertEquals("JAVA", students[0].getGroup());

        assertEquals("Petar Todorov", students[1].getName());
        assertEquals(20, students[1].getAge());
        assertEquals("JAVA", students[1].getGroup());

        assertEquals("Georgi Yordanov", students[2].getName());
        assertEquals(25, students[2].getAge());
        assertEquals("PYTHON", students[2].getGroup());
    }

    @Test
    void getStudentThrowsException() {
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.getForEntity(getBaseUrl() + "/INVALID_ID", StudentDTO.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        String expectedMessage =
                "{\"status\":400,\"message\":\"Method parameter 'id': " +
                "Failed to convert value of type 'java.lang.String' to required type 'int'; " +
                "For input string: \\\"INVALID_ID\\\"\"}";
        assertEquals(expectedMessage, exception.getResponseBodyAsString());
    }

    @Test
    void updateStudent() {
        StudentDTO studentDTO = MockUtil.mockStudentDTO(INVALID_ID, NAME, AGE, GROUP);
        ResponseEntity<StudentDTO> response =
                restTemplate.postForEntity(getBaseUrl(), studentDTO, StudentDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NAME, response.getBody().getName());

        int id = response.getBody().getId();
        String newName = "Petar Petrov";

        studentDTO.setName(newName);

        response = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(studentDTO),
                StudentDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(newName, response.getBody().getName());
    }


    @Test
    void deleteStudent() {
        ResponseEntity<StudentDTO> createResponse =
                restTemplate.postForEntity(getBaseUrl(), mockedStudentDTO, StudentDTO.class);

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
                "{\"status\":404,\"message\":\"Student with id = " + id + " not found.\"}";
        assertEquals(expectedMessage, exception.getResponseBodyAsString());
    }

    @Test
    @Sql(scripts = {"/students-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void countStudents() {
        ResponseEntity<Long> response = restTemplate.getForEntity(getBaseUrl() + "/count", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody());
    }
}
