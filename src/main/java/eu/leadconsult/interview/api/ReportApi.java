package eu.leadconsult.interview.api;

import eu.leadconsult.interview.dto.PersonDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(
        name = "Reports Rest API",
        description = "Handles different kind of reports related to students, teachers and courses."
)
@RequestMapping("/api/reports")
public interface ReportApi {

    @Operation(summary = "Get students by course.", description = "Find which students participate in specific course.")
    @GetMapping("/studentsByCourse/{courseId}")
    List<StudentDTO> getStudentsByCourse(@PathVariable int courseId);

    @Operation(summary = "Get students by group.", description = "Find which students participate in specific group.")
    @GetMapping("/studentsByGroup/{group}")
    List<StudentDTO> getStudentsByGroup(@PathVariable String group);

    @Operation(
            summary = "Get students by course and age.",
            description = "Find all students older than specific age and participate in specific course.")
    @GetMapping("/studentsByCourseAndAge")
    List<StudentDTO> getStudentsByCourseAndAge(@RequestParam int courseId, @RequestParam int age);


    @Operation(
            summary = "Find all by course and group.",
            description = "Find all teachers and students for specific group and course.")
    @GetMapping("/findAllByCourseAndGroup")
    List<PersonDTO> findAllByCourseAndGroup(@RequestParam int courseId, @RequestParam String group);


}
