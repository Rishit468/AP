package edu.univ.erp.data;

import edu.univ.erp.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErpDao {

    // ======================================================
    // STUDENT OPERATIONS
    // ======================================================
    public Student getStudentByUserId(int userId) {
        String sql = "SELECT * FROM students WHERE user_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Student s = new Student();
                s.setUserId(rs.getInt("user_id"));
                s.setRollNo(rs.getString("roll_no"));
                s.setProgram(rs.getString("program"));
                s.setYear(rs.getInt("year"));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY roll_no";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Student s = new Student();
                s.setUserId(rs.getInt("user_id"));
                s.setRollNo(rs.getString("roll_no"));
                s.setProgram(rs.getString("program"));
                s.setYear(rs.getInt("year"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ======================================================
    // INSTRUCTOR OPERATIONS
    // ======================================================
    public Instructor getInstructorByUserId(int userId) {
        String sql = "SELECT * FROM instructors WHERE user_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Instructor i = new Instructor();
                i.setInstructorId(rs.getInt("instructor_id"));
                i.setUserId(rs.getInt("user_id"));
                i.setName(rs.getString("name"));
                i.setEmail(rs.getString("email"));
                i.setDepartment(rs.getString("department"));
                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Instructor> getAllInstructors() {
        List<Instructor> list = new ArrayList<>();
        String sql = "SELECT * FROM instructors ORDER BY name";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Instructor i = new Instructor();
                i.setInstructorId(rs.getInt("instructor_id"));
                i.setUserId(rs.getInt("user_id"));
                i.setName(rs.getString("name"));
                i.setEmail(rs.getString("email"));
                i.setDepartment(rs.getString("department"));
                list.add(i);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Course> getCoursesByInstructor(int instructorId) {
        List<Course> list = new ArrayList<>();
        String sql = """
            SELECT DISTINCT c.*
            FROM courses c
            JOIN sections s ON c.course_id = s.course_id
            WHERE s.instructor_id = ?
        """;
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, instructorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCode(rs.getString("code"));
                c.setTitle(rs.getString("title"));
                c.setCredits(rs.getInt("credits"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Student> getStudentsByInstructor(int instructorId) {
        List<Student> list = new ArrayList<>();
        String sql = """
            SELECT DISTINCT st.*
            FROM students st
            JOIN enrollments e ON st.student_id = e.student_id
            JOIN sections s ON e.section_id = s.section_id
            WHERE s.instructor_id = ?
        """;
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, instructorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setUserId(rs.getInt("user_id"));
                s.setRollNo(rs.getString("roll_no"));
                s.setProgram(rs.getString("program"));
                s.setYear(rs.getInt("year"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ======================================================
    // COURSE OPERATIONS
    // ======================================================
    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY code";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCode(rs.getString("code"));
                c.setTitle(rs.getString("title"));
                c.setCredits(rs.getInt("credits"));
                list.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Course getCourseById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, courseId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("course_id"));
                c.setCode(rs.getString("code"));
                c.setTitle(rs.getString("title"));
                c.setCredits(rs.getInt("credits"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======================================================
    // SECTION OPERATIONS
    // ======================================================
    public List<Section> getAllSections() {
        List<Section> list = new ArrayList<>();
        String sql = "SELECT * FROM sections ORDER BY section_id";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Section s = new Section();
                s.setSectionId(rs.getInt("section_id"));
                s.setCourseId(rs.getInt("course_id"));
                s.setInstructorId(rs.getInt("instructor_id"));
                s.setDayTime(rs.getString("day_time"));
                s.setRoom(rs.getString("room"));
                s.setCapacity(rs.getInt("capacity"));
                s.setSemester(rs.getString("semester"));
                s.setYear(rs.getInt("year"));
                s.setDropDeadline(rs.getDate("drop_deadline"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Section> getSectionsByInstructor(int instructorId) {
        List<Section> list = new ArrayList<>();
        String sql = "SELECT * FROM sections WHERE instructor_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, instructorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Section s = new Section();
                s.setSectionId(rs.getInt("section_id"));
                s.setCourseId(rs.getInt("course_id"));
                s.setInstructorId(rs.getInt("instructor_id"));
                s.setDayTime(rs.getString("day_time"));
                s.setRoom(rs.getString("room"));
                s.setCapacity(rs.getInt("capacity"));
                s.setSemester(rs.getString("semester"));
                s.setYear(rs.getInt("year"));
                s.setDropDeadline(rs.getDate("drop_deadline"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ======================================================
    // ENROLLMENT OPERATIONS
    // ======================================================
    public List<Enrollment> getEnrollmentsByStudent(int UserId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = """
            SELECT e.*, c.code AS course_code, c.title AS course_title
            FROM enrollments e
            JOIN sections s ON e.section_id = s.section_id
            JOIN courses c ON s.course_id = c.course_id
            WHERE e.student_id = ?
        """;
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, UserId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollmentId(rs.getInt("enrollment_id"));
                e.setSectionId(rs.getInt("section_id"));
                e.setStatus(rs.getString("status"));
                e.setCourseCode(rs.getString("course_code"));
                e.setCourseTitle(rs.getString("course_title"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Enrollment> getEnrollmentsBySection(int sectionId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = """
            SELECT e.*, st.roll_no, u.username AS student_name
            FROM enrollments e
            JOIN students st ON e.student_id = st.student_id
            JOIN auth_db.users u ON st.user_id = u.user_id
            WHERE e.section_id = ?
        """;
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, sectionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollmentId(rs.getInt("enrollment_id"));
                e.setSectionId(rs.getInt("section_id"));
                e.setStatus(rs.getString("status"));
                e.setStudentName(rs.getString("student_name"));
                e.setRollNo(rs.getString("roll_no"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ======================================================
    // STUDENT ENROLLMENT ACTIONS
    // ======================================================
    public boolean enrollStudent(int studentId, int sectionId) {
        String sql = "INSERT INTO enrollments (student_id, section_id, status) VALUES (?, ?, 'ENROLLED')";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, sectionId);
            return ps.executeUpdate() == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Student already enrolled or invalid foreign key.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean dropEnrollment(int studentId, int sectionId) {
        String sql = "DELETE FROM enrollments WHERE student_id = ? AND section_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ps.setInt(2, sectionId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ======================================================
    // ADMIN HELPERS
    // ======================================================
    public boolean addCourse(String code, String title, int credits) {
        String sql = "INSERT INTO courses (code, title, credits) VALUES (?, ?, ?)";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            ps.setString(2, title);
            ps.setInt(3, credits);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM courses WHERE course_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
