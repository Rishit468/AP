CREATE DATABASE IF NOT EXISTS erp_db;
USE erp_db;

CREATE TABLE students (
  user_id INT PRIMARY KEY,
  roll_no VARCHAR(50) NOT NULL,
  program VARCHAR(100),
  year INT,
  FOREIGN KEY (user_id) REFERENCES auth_db.users(user_id) ON DELETE CASCADE
);

CREATE TABLE instructors (
  user_id INT PRIMARY KEY,
  department VARCHAR(100),
  FOREIGN KEY (user_id) REFERENCES auth_db.users(user_id) ON DELETE CASCADE
);

CREATE TABLE courses (
  course_id INT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(20) UNIQUE NOT NULL,
  title VARCHAR(255) NOT NULL,
  credits INT NOT NULL
);

CREATE TABLE sections (
  section_id INT PRIMARY KEY AUTO_INCREMENT,
  course_id INT NOT NULL,
  instructor_id INT,
  day_time VARCHAR(100),
  room VARCHAR(50),
  capacity INT NOT NULL,
  semester VARCHAR(20),
  year INT,
  drop_deadline DATE,
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (instructor_id) REFERENCES instructors(user_id) ON DELETE SET NULL
);

CREATE TABLE enrollments (
  enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id INT NOT NULL,
  section_id INT NOT NULL,
  status ENUM('ENROLLED','DROPPED','COMPLETED') DEFAULT 'ENROLLED',
  UNIQUE KEY uq_student_section (student_id, section_id),
  FOREIGN KEY (student_id) REFERENCES students(user_id) ON DELETE CASCADE,
  FOREIGN KEY (section_id) REFERENCES sections(section_id) ON DELETE CASCADE
);

CREATE TABLE grades (
  grade_id INT PRIMARY KEY AUTO_INCREMENT,
  enrollment_id INT NOT NULL,
  component VARCHAR(100),
  score DOUBLE,
  FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);

CREATE TABLE settings (
  `key` VARCHAR(100) PRIMARY KEY,
  `value` VARCHAR(200)
);
