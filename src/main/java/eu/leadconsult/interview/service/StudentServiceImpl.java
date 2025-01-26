package eu.leadconsult.interview.service;

import eu.leadconsult.interview.dao.StudentRepository;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.entity.Student;
import eu.leadconsult.interview.exception.EntityNotFoundException;
import eu.leadconsult.interview.mapper.StudentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        student.setId(0);

        return studentMapper.studentToStudentDTO(studentRepository.save(student));
    }

    @Override
    public StudentDTO getStudent(int id) {
        Student student = findStudentOrThrow(id);

        return studentMapper.studentToStudentDTO(student);
    }

    @Override
    public List<StudentDTO> getStudents() {
        return studentRepository
                .findAll().stream()
                .map(studentMapper::studentToStudentDTO)
                .toList();
    }

    @Override
    public List<StudentDTO> getStudentsByGroup(String group) {
        return studentRepository.findByGroupName(group).stream()
                .map(studentMapper::studentToStudentDTO)
                .toList();
    }

    @Override
    public StudentDTO updateStudent(int id, StudentDTO studentDTO) {
        findStudentOrThrow(id);
        Student student = studentMapper.studentDTOToStudent(studentDTO);
        student.setId(id);

        return studentMapper.studentToStudentDTO(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(int id) {
        findStudentOrThrow(id);
        studentRepository.deleteById(id);
    }

    @Override
    public long countStudents() {
        return studentRepository.count();
    }

    @Override
    public Student findStudentOrThrow(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with id = %d not found.".formatted(id)));
    }
}
