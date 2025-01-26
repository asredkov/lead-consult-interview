package eu.leadconsult.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "Student details")
@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends PersonDetails {
}
