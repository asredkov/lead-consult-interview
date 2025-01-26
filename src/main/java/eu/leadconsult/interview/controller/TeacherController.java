package eu.leadconsult.interview.controller;

import eu.leadconsult.interview.api.TeacherApi;
import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.service.TeacherService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TeacherController implements TeacherApi {
    private final TeacherService teacherService;

    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        return teacherService.createTeacher(teacherDTO);
    }

    @Override
    public List<TeacherDTO> getTeachers() {
        return teacherService.getTeachers();
    }

    @Override
    public TeacherDTO getTeacher(int id) {
        return teacherService.getTeacher(id);
    }

    @Override
    public TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO) {
        return teacherService.updateTeacher(id, teacherDTO);
    }

    @Override
    public void deleteTeacher(int id) {
        teacherService.deleteTeacher(id);
    }

    @Override
    public long countTeachers() {
        return teacherService.countTeachers();
    }
}
