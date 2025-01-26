package eu.leadconsult.interview.dao;

import eu.leadconsult.interview.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByGroupName(String groupName);
}
