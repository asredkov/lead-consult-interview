package eu.leadconsult.interview.mapper;

import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.entity.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
    @Mapping(target = "groupName", source = "group")
    Teacher teacherDTOToTeacher(TeacherDTO teacherDTO);

    @Mapping(target = "group", source = "groupName")
    TeacherDTO teacherToTeacherDTO(Teacher teacher);
}

