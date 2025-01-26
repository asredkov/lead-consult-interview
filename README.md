# Lead Consult Interview Task

## Task
Implement an application for storing Students and Teachers information like name, age, group and
courses. We have two type of courses -Main and Secondary.
The application should be able to add remove or modify students and teachers. With this application we
should be able to create different reports :
* how many students we have
* how many teachers we have
* how many courses by type we have
* which students participate in specific course
* which students participate in specific group
* find all teachers and students for specific group and course
* find all students older than specific age and participate in specific course
* Please write tests and use an in-memory database.

The application should provide public API.

## Solution
The solution is implemented using Spring Boot, Spring Data JPA, H2 in-memory database and JUnit 5 for testing.
Additional dependencies like openapi, lombok, and mapstruct are used. 

Project stub is generated using [Spring Initializr](https://start.spring.io/).

### OpenAPI (Swagger-UI and API documentation)
The purpose of the openapi is to provide a documentation for the API and swagger UI to test the API.

API endpoints can be tests on Swagger-UI interface on the following url: http://localhost:8080/swagger-ui/index.html

### Lombok
Lombok is used to reduce boilerplate code like generating constructors, setter, getter, etc.

### Mapstruct
Mapstruct is used to map entities to DTOs and vice versa.

### Global error handler
There is global error handler `RestExceptionHandler` used to handle exceptions and return a proper response to the client.

### Running the application (without Data)
#### IDE:
Run `InterviewApplication` as java application.
#### GIT BASH:
Execute following command in GIT BASH: `mvn spring-boot:run`

### Running the application with predefined Data
#### IDE:
Set the active profile to: `initial-data`.
Run `InterviewApplication` as java application.

#### GIT BASH:
Execute following command in GIT BASH: `mvn spring-boot:run -Dspring-boot.run.profiles=initial-data`