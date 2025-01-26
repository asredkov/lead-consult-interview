package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.dto.TeacherDTO;
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
public class TeacherControllerIT {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/teachers";
    }

    private final int INVALID_ID = 100;
    private final String NAME = "Ivan Ivanov";
    private final int AGE = 35;
    private final String GROUP = "PYTHON";
    private final TeacherDTO mockedTeacherDTO = MockUtil.mockTeacherDTO(INVALID_ID, NAME, AGE, GROUP);

    @Test
    void createAndGetTeacher() {
        ResponseEntity<TeacherDTO> response =
                restTemplate.postForEntity(getBaseUrl(), mockedTeacherDTO, TeacherDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        int actualId = response.getBody().getId();
        assertNotEquals(INVALID_ID, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(AGE, response.getBody().getAge());
        assertEquals(GROUP, response.getBody().getGroup());

        response = restTemplate.getForEntity(getBaseUrl() + "/" + actualId, TeacherDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(actualId, response.getBody().getId());
        assertEquals(NAME, response.getBody().getName());
        assertEquals(AGE, response.getBody().getAge());
        assertEquals(GROUP, response.getBody().getGroup());
    }

    @Test
    void getTeacherThrowsException() {
        HttpClientErrorException exception = assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.getForEntity(getBaseUrl() + "/INVALID_ID", TeacherDTO.class)
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        String expectedMessage =
                "{\"status\":400,\"message\":\"Method parameter 'id': " +
                "Failed to convert value of type 'java.lang.String' to required type 'int'; " +
                "For input string: \\\"INVALID_ID\\\"\"}";
        assertEquals(expectedMessage, exception.getResponseBodyAsString());
    }

    @Test
    void updateTeacher() {
        TeacherDTO teacherDTO = MockUtil.mockTeacherDTO(INVALID_ID, NAME, AGE, GROUP);
        ResponseEntity<TeacherDTO> response =
                restTemplate.postForEntity(getBaseUrl(), teacherDTO, TeacherDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(NAME, response.getBody().getName());

        int id = response.getBody().getId();
        String newName = "Petar Petrov";

        teacherDTO.setName(newName);

        response = restTemplate.exchange(
                getBaseUrl() + "/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(teacherDTO),
                TeacherDTO.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(id, response.getBody().getId());
        assertEquals(newName, response.getBody().getName());
    }


    @Test
    void deleteTeacher() {
        ResponseEntity<TeacherDTO> createResponse =
                restTemplate.postForEntity(getBaseUrl(), mockedTeacherDTO, TeacherDTO.class);

        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertEquals(NAME, createResponse.getBody().getName());

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
                "{\"status\":404,\"message\":\"Teacher with id = " + id + " not found.\"}";
        assertEquals(expectedMessage, exception.getResponseBodyAsString());
    }

    @Test
    @Sql(scripts = {"/teachers-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void countTeachers() {
        ResponseEntity<Long> response = restTemplate.getForEntity(getBaseUrl() + "/count", Long.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody());
    }

    @Test
    @Sql(scripts = {"/teachers-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void getTeachers() {
        ResponseEntity<TeacherDTO[]> response =
                restTemplate.getForEntity(getBaseUrl(), TeacherDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().length);

        TeacherDTO[] teachers = response.getBody();
        assertEquals("Kalin Dimitrov", teachers[0].getName());
        assertEquals(38, teachers[0].getAge());
        assertEquals("JAVA", teachers[0].getGroup());

        assertEquals("Dimitar Georgiev", teachers[1].getName());
        assertEquals(40, teachers[1].getAge());
        assertEquals("JAVA", teachers[1].getGroup());
    }

}
