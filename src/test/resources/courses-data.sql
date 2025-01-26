DELETE FROM course_students;
DELETE FROM course_teachers;

DELETE FROM student;
INSERT INTO student (name, age, group_name) VALUES ('Ivan Ivanov', 22, 'JAVA');
INSERT INTO student (name, age, group_name) VALUES ('Petar Todorov', 20, 'JAVA');
INSERT INTO student (name, age, group_name) VALUES ('Georgi Yordanov', 25, 'PYTHON');

DELETE FROM teacher;
INSERT INTO teacher (name, age, group_name) VALUES ('Kalin Dimitrov', 38, 'JAVA');
INSERT INTO teacher (name, age, group_name) VALUES ('Dimitar Georgiev', 40, 'JAVA');

DELETE FROM course;
INSERT INTO course (title, description, type) VALUES ('Java Basics', 'Introduction into JAVA basics.', 0);
INSERT INTO course (title, description, type) VALUES ('C# Basics', 'Introduction into C# basics.', 0);
INSERT INTO course (title, description, type) VALUES ('Data Structure and Algorithms', 'Introduction to Data Structures and Algorithms.', 0);
INSERT INTO course (title, description, type) VALUES ('Design Patterns', 'Gangs of Four Design Patterns.', 1);