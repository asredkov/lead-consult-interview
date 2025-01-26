package eu.leadconsult.interview.dto;

import eu.leadconsult.interview.dto.enums.PersonType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Schema(description = "Person details")
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class PersonDTO extends PersonDetails {
    private PersonType type;
}
