package eu.leadconsult.interview.mapper;

import eu.leadconsult.interview.dto.PersonDTO;
import eu.leadconsult.interview.entity.Student;
import eu.leadconsult.interview.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    @Mapping(target = "type", expression = "java(PersonType.STUDENT)")
    @Mapping(target = "group", source = "groupName")
    PersonDTO studentToPersonDTO(Student student);

    @Mapping(target = "type", expression = "java(PersonType.TEACHER)")
    @Mapping(target = "group", source = "groupName")
    PersonDTO teacherToPersonDTO(Teacher teacher);
}
