package eu.leadconsult.interview.dao;

import eu.leadconsult.interview.dto.enums.CourseType;
import eu.leadconsult.interview.entity.Course;
import eu.leadconsult.interview.entity.Student;
import eu.leadconsult.interview.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    long countByType(CourseType type);


    @Query("SELECT s FROM Course c JOIN c.students s WHERE c.id = :courseId AND s.age > :age")
    Set<Student> findStudentsByCourseAndAge(@Param("courseId") int courseId, @Param("age") int age);

    @Query("SELECT s FROM Course c JOIN c.students s WHERE c.id = :courseId AND s.groupName = :group")
    Set<Student> findStudentsByCourseAndGroup(@Param("courseId") int courseId, @Param("group") String group);

    @Query("SELECT t FROM Course c JOIN c.teachers t WHERE c.id = :courseId AND t.groupName = :group")
    Set<Teacher> findTeachersByCourseAndGroup(@Param("courseId") int courseId, @Param("group") String group);
}
