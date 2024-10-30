DROP DATABASE IF EXISTS cgpa_management;
CREATE DATABASE cgpa_management;
USE cgpa_management;

CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    cgpa FLOAT DEFAULT 0
);

CREATE TABLE grades (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT,
    subject VARCHAR(100),
    grade VARCHAR(2),
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);
SHOW tables;