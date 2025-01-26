package eu.leadconsult.interview.service;

import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.entity.Teacher;

import java.util.List;

public interface TeacherService {
    TeacherDTO createTeacher(TeacherDTO teacherDTO);

    TeacherDTO getTeacher(int id);

    List<TeacherDTO> getTeachers();

    TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO);

    void deleteTeacher(int id);

    long countTeachers();

    Teacher findTeacherOrThrow(int id);
}
