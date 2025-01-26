package eu.leadconsult.interview.service;

import eu.leadconsult.interview.dao.CourseRepository;
import eu.leadconsult.interview.dto.CourseDTO;
import eu.leadconsult.interview.dto.PersonDTO;
import eu.leadconsult.interview.dto.StudentDTO;
import eu.leadconsult.interview.dto.enums.CourseType;
import eu.leadconsult.interview.entity.Course;
import eu.leadconsult.interview.entity.Student;
import eu.leadconsult.interview.entity.Teacher;
import eu.leadconsult.interview.exception.EntityAlreadyExistsException;
import eu.leadconsult.interview.exception.EntityNotFoundException;
import eu.leadconsult.interview.mapper.CourseMapper;
import eu.leadconsult.interview.mapper.PersonMapper;
import eu.leadconsult.interview.mapper.StudentMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;
    private final PersonMapper personMapper;

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.courseDTOToCourse(courseDTO);
        course.setId(0);

        return courseMapper.courseToCourseDTO(courseRepository.save(course));
    }

    @Override
    public List<CourseDTO> getCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::courseToCourseDTO)
                .toList();
    }

    @Override
    public CourseDTO getCourse(int id) {
        Course course = findCourseOrThrow(id);

        return courseMapper.courseToCourseDTO(course);
    }

    @Override
    public CourseDTO updateCourse(int id, CourseDTO courseDTO) {
        findCourseOrThrow(id);
        Course course = courseMapper.courseDTOToCourse(courseDTO);
        course.setId(id);

        return courseMapper.courseToCourseDTO(courseRepository.save(course));
    }

    @Override
    public void deleteCourse(int id) {
        findCourseOrThrow(id);
        courseRepository.deleteById(id);
    }

    @Override
    public long countByType(CourseType type) {
        return courseRepository.countByType(type);
    }

    @Override

    public void addStudentToCourse(int courseId, int studentId) {
        Course course = findCourseOrThrow(courseId);
        Student student = studentService.findStudentOrThrow(studentId);

        Set<Student> students = course.getStudents();
        if (students.contains(student)) {
            throw new EntityAlreadyExistsException(
                    "Student '%s' with id = %d is already enrolled to course '%s' with id = %d"
                            .formatted(student.getName(), studentId, course.getTitle(), courseId)
            );
        }
        students.add(student);

        courseRepository.save(course);
    }

    @Override
    public void addTeacherToCourse(int courseId, int teacherId) {
        Course course = findCourseOrThrow(courseId);
        Teacher teacher = teacherService.findTeacherOrThrow(teacherId);

        Set<Teacher> teachers = course.getTeachers();
        if (teachers.contains(teacher)) {
            throw new EntityAlreadyExistsException(
                    "Teacher '%s' with id = %d is already assigned to course '%s' with id = %d"
                            .formatted(teacher.getName(), teacherId, course.getTitle(), courseId)
            );
        }
        teachers.add(teacher);

        courseRepository.save(course);
    }

    @Override
    public Course findCourseOrThrow(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course with id = %d not found.".formatted(id)));
    }

    @Override
    public List<PersonDTO> findAllByCourseAndGroup(int courseId, String group) {
        List<PersonDTO> students = courseRepository.findStudentsByCourseAndGroup(courseId, group).stream()
                .map(personMapper::studentToPersonDTO)
                .toList();

        List<PersonDTO> teachers = courseRepository.findTeachersByCourseAndGroup(courseId, group).stream()
                .map(personMapper::teacherToPersonDTO)
                .toList();

        return Stream.concat(students.stream(), teachers.stream()).toList();
    }

    @Override
    public List<StudentDTO> getStudentsByCourseAndAge(int courseId, int age) {
        return courseRepository.findStudentsByCourseAndAge(courseId, age).stream()
                .map(studentMapper::studentToStudentDTO)
                .toList();
    }
}
