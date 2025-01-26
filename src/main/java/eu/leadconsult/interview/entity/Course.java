package eu.leadconsult.interview.entity;

import eu.leadconsult.interview.dto.enums.CourseType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private CourseType type;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToMany
    Set<Student> students;

    @ManyToMany
    Set<Teacher> teachers;
}
