package edu.univ.erp.data;

import edu.univ.erp.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ErpDao {

    // ---------------------------
    // STUDENT OPERATIONS
    // ---------------------------
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

    // ---------------------------
    // INSTRUCTOR OPERATIONS
    // ---------------------------
    public Instructor getInstructorByUserId(int userId) {
        String sql = "SELECT * FROM instructors WHERE user_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Instructor i = new Instructor();
                i.setUserId(rs.getInt("user_id"));
                i.setDepartment(rs.getString("department"));
                return i;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ---------------------------
    // COURSE OPERATIONS
    // ---------------------------
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

    // ---------------------------
    // SECTION OPERATIONS
    // ---------------------------
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

    public List<Section> getAllSections() {
        List<Section> list = new ArrayList<>();
        String sql = "SELECT * FROM sections";
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

    // ---------------------------
    // ENROLLMENT OPERATIONS
    // ---------------------------
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollments WHERE student_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollmentId(rs.getInt("enrollment_id"));
                e.setStudentId(rs.getInt("student_id"));
                e.setSectionId(rs.getInt("section_id"));
                e.setStatus(rs.getString("status"));
                list.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean enrollStudent(int studentId, int sectionId) {
        String sql = "INSERT INTO enrollments (student_id, section_id, status) VALUES (?, ?, 'ENROLLED')";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            ps.setInt(2, sectionId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error enrolling student: " + e.getMessage());
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
            System.err.println("Error dropping enrollment: " + e.getMessage());
        }
        return false;
    }

    // ---------------------------
    // GRADE OPERATIONS
    // ---------------------------
    public List<GradeComponent> getGradesByEnrollment(int enrollmentId) {
        List<GradeComponent> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE enrollment_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                GradeComponent g = new GradeComponent();
                g.setGradeId(rs.getInt("grade_id"));
                g.setEnrollmentId(rs.getInt("enrollment_id"));
                g.setComponent(rs.getString("component"));
                g.setScore(rs.getDouble("score"));
                list.add(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
