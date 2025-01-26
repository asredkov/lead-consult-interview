package eu.leadconsult.interview.service;

import eu.leadconsult.interview.dao.TeacherRepository;
import eu.leadconsult.interview.dto.TeacherDTO;
import eu.leadconsult.interview.entity.Teacher;
import eu.leadconsult.interview.exception.EntityNotFoundException;
import eu.leadconsult.interview.mapper.TeacherMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    public TeacherDTO createTeacher(TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
        teacher.setId(0);

        return teacherMapper.teacherToTeacherDTO(teacherRepository.save(teacher));
    }

    @Override
    public TeacherDTO getTeacher(int id) {
        Teacher teacher = findTeacherOrThrow(id);

        return teacherMapper.teacherToTeacherDTO(teacher);
    }

    @Override
    public List<TeacherDTO> getTeachers() {
        return teacherRepository
                .findAll().stream()
                .map(teacherMapper::teacherToTeacherDTO)
                .toList();
    }

    @Override
    public TeacherDTO updateTeacher(int id, TeacherDTO teacherDTO) {
        findTeacherOrThrow(id);
        Teacher teacher = teacherMapper.teacherDTOToTeacher(teacherDTO);
        teacher.setId(id);

        return teacherMapper.teacherToTeacherDTO(teacherRepository.save(teacher));
    }

    @Override
    public void deleteTeacher(int id) {
        findTeacherOrThrow(id);
        teacherRepository.deleteById(id);
    }

    @Override
    public long countTeachers() {
        return teacherRepository.count();
    }

    @Override
    public Teacher findTeacherOrThrow(int id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Teacher with id = %d not found.".formatted(id)));
    }
}
