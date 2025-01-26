package eu.leadconsult.interview.api;

import eu.leadconsult.interview.dto.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Students Rest API", description = "Handles students related operations.")
@RequestMapping("/api/students")
public interface StudentApi {

    @Operation(summary = "Add new student.")
    @PostMapping
    StudentDTO createStudent(@RequestBody StudentDTO student);

    @Operation(summary = "Get all students.", description = "List all of the students.")
    @GetMapping
    List<StudentDTO> getStudents();

    @Operation(summary = "Retrieve student.", description = "Retrieve student details by specific id.")
    @GetMapping("/{id}")
    StudentDTO getStudent(@PathVariable int id);

    @Operation(summary = "Update existing student.", description = "Update existing student by given id.")
    @PutMapping("/{id}")
    StudentDTO updateStudent(@PathVariable int id, @RequestBody StudentDTO student);

    @Operation(summary = "Delete existing student.", description = "Delete existing student by given id.")
    @DeleteMapping("/{id}")
    void deleteStudent(@PathVariable int id);

    @Operation(summary = "Get students count.", description = "Get total number of students.")
    @GetMapping("/count")
    long countStudents();


}
