package eu.leadconsult.interview.dao;

import eu.leadconsult.interview.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
