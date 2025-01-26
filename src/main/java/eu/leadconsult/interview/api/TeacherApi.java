package eu.leadconsult.interview.api;

import eu.leadconsult.interview.dto.TeacherDTO;
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

@Tag(name = "Teachers Rest API", description = "Handles teachers related operations.")
@RequestMapping("/api/teachers")
public interface TeacherApi {
    @Operation(summary = "Add new teacher.")
    @PostMapping
    TeacherDTO createTeacher(@RequestBody TeacherDTO teacherDTO);

    @Operation(summary = "Get all teachers.", description = "List all of the teachers.")
    @GetMapping
    List<TeacherDTO> getTeachers();

    @Operation(summary = "Retrieve teacher.", description = "Retrieve teacher details by specific id.")
    @GetMapping("/{id}")
    TeacherDTO getTeacher(@PathVariable int id);

    @Operation(summary = "Update existing teacher.", description = "Update existing teacher by given id.")
    @PutMapping("/{id}")
    TeacherDTO updateTeacher(@PathVariable int id, @RequestBody TeacherDTO teacherDTO);

    @Operation(summary = "Delete existing teacher.", description = "Delete existing teacher by given id.")
    @DeleteMapping("/{id}")
    void deleteTeacher(@PathVariable int id);

    @Operation(summary = "Get teachers count.", description = "Get total number of teachers.")
    @GetMapping("/count")
    long countTeachers();
}
