package eu.leadconsult.interview.api;

import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.enums.CourseType;
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

@Tag(name = "Courses Rest API", description = "Handles courses related operations.")
@RequestMapping("/api/courses")
public interface CourseApi {
    @Operation(summary = "Add new course.")
    @PostMapping
    CourseDTO createCourse(@RequestBody CourseDTO course);

    @Operation(summary = "Get all courses.", description = "List all of the courses.")
    @GetMapping
    List<CourseDTO> getCourses();

    @Operation(summary = "Retrieve course.", description = "Retrieve course details by specific id.")
    @GetMapping("/{id}")
    CourseDTO getCourse(@PathVariable int id);

    @Operation(summary = "Update existing course.", description = "Update existing course by given id.")
    @PutMapping("/{id}")
    CourseDTO updateCourse(@PathVariable int id, @RequestBody CourseDTO student);

    @Operation(summary = "Delete existing course.", description = "Delete existing course by given id.")
    @DeleteMapping("/{id}")
    void deleteCourse(@PathVariable int id);

    @Operation(
            summary = "Get course count by type.",
            description = "Get count of courses by specific type (Main or Secondary)."
    )
    @GetMapping("/countByType/{type}")
    long countByType(@PathVariable("type") CourseType type);

    @Operation(
            summary = "Add student to course.",
            description = "Enroll student with specific id to course with specific id."
    )
    @PostMapping("/{courseId}/student/{studentId}")
    void addStudentToCourse(@PathVariable int courseId, @PathVariable int studentId);

    @Operation(
            summary = "Add student to course.",
            description = "Assign teacher with specific id to course with specific id."
    )
    @PostMapping("/{courseId}/teacher/{teacherId}")
    void addTeacherToCourse(@PathVariable int courseId, @PathVariable int teacherId);
}
