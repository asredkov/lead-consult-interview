package eu.leadconsult.interview.mapper;

import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(target = "groupName", source = "group")
    Student studentDTOToStudent(StudentDTO studentDTO);

    @Mapping(target = "group", source = "groupName")
    StudentDTO studentToStudentDTO(Student student);
}

