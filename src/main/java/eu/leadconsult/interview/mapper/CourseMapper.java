package eu.leadconsult.interview.mapper;

import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course courseDTOToCourse(CourseDTO courseDTO);
    CourseDTO courseToCourseDTO(Course course);
}
