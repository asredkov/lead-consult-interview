package eu.leadconsult.interview.dto;

import eu.leadconsult.interview.dto.enums.CourseType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Course details")
@Data
public class CourseDTO {
    private int id;
    private String title;
    private String description;
    private CourseType type;
}
