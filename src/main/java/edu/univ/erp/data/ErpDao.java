package edu.univ.erp.data;

import edu.univ.erp.domain.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // ======================================================
    // INSTRUCTOR OPERATIONS
    // ======================================================
    public Instructor getInstructorByUserId(int userId) {
        String sql = "SELECT * FROM instructors WHERE user_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Instructor i = new Instructor();
                    i.setInstructorId(rs.getInt("instructor_id"));
                    i.setUserId(rs.getInt("user_id"));
                    i.setName(rs.getString("name"));
                    i.setEmail(rs.getString("email"));
                    i.setDepartment(rs.getString("department"));
                    return i;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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

    public Section getSectionById(int sectionId) {
        String sql = "SELECT * FROM sections WHERE section_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            try(ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
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
                    return s;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ======================================================
    // ENROLLMENT OPERATIONS
    // ======================================================

    public Map<Integer, Map<String, Double>> getGradesBySection(int sectionId) {
        Map<Integer, Map<String, Double>> allGrades = new HashMap<>();

        String sql = "SELECT g.enrollment_id, g.component, g.score " +
                "FROM grades g " +
                "JOIN enrollments e ON g.enrollment_id = e.enrollment_id " +
                "WHERE e.section_id = ?";

        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int enrollmentId = rs.getInt("enrollment_id");
                    String component = rs.getString("component");
                    double score = rs.getDouble("score");

                    // Get or create the map for this enrollmentId
                    allGrades.computeIfAbsent(enrollmentId, k -> new HashMap<>())
                            .put(component, score);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allGrades;
    }

    public void upsertGrade(int enrollmentId, String component, Double score) throws SQLException {
        // Use database-specific syntax for "upsert"
        // This is for MySQL: INSERT ... ON DUPLICATE KEY UPDATE
        String sql = "INSERT INTO grades (enrollment_id, component, score) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE score = VALUES(score)";

        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, enrollmentId);
            ps.setString(2, component);
            if (score == null) {
                ps.setNull(3, Types.DOUBLE);
            } else {
                ps.setDouble(3, score);
            }

            ps.executeUpdate();
        }
        // Let SQLException propagate
    }
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.section_id, e.status, " +
                "s.day_time, s.room, " +
                "c.code AS course_code, c.title AS course_title, " +
                "i.name AS instructor_name " +
                "FROM enrollments e " +
                "JOIN sections s ON e.section_id = s.section_id " +
                "JOIN courses c ON s.course_id = c.course_id " +
                "LEFT JOIN instructors i ON s.instructor_id = i.instructor_id " +
                "WHERE e.student_id = ? " +
                "ORDER BY c.code";

        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment();
                    e.setEnrollmentId(rs.getInt("enrollment_id"));
                    e.setStudentId(studentId); // Not in SELECT, but we know it
                    e.setSectionId(rs.getInt("section_id"));
                    e.setStatus(rs.getString("status"));
                    // Joined data
                    e.setCourseCode(rs.getString("course_code"));
                    e.setCourseTitle(rs.getString("course_title"));
                    e.setDayTime(rs.getString("day_time"));
                    e.setRoom(rs.getString("room"));
                    e.setInstructorName(rs.getString("instructor_name"));
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Grade> getGradesByEnrollment(int enrollmentId) {
        List<Grade> list = new ArrayList<>();
        String sql = "SELECT * FROM grades WHERE enrollment_id = ? ORDER BY component";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, enrollmentId);
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Grade g = new Grade();
                    g.setGradeId(rs.getInt("grade_id"));
                    g.setEnrollmentId(rs.getInt("enrollment_id"));
                    g.setComponent(rs.getString("component"));
                    g.setScore(rs.getDouble("score"));
                    list.add(g);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getEnrollmentCountBySection(int sectionId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE section_id = ?";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sectionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public List<Enrollment> getEnrollmentsBySection(int sectionId) {
        List<Enrollment> list = new ArrayList<>();
        String sql = """
            SELECT e.*, st.roll_no, u.username AS student_name
            FROM enrollments e
            JOIN students st ON e.student_id = st.user_id
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
    public boolean enrollStudent(int studentId, int sectionId) throws SQLException {
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

    public boolean dropEnrollment(int enrollmentId) throws SQLException {
        // Also delete associated grades to prevent orphaned data
        String sqlGrades = "DELETE FROM grades WHERE enrollment_id = ?";
        String sqlEnroll = "DELETE FROM enrollments WHERE enrollment_id = ?";

        try (Connection conn = DbPool.getErpDataSource().getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            // 1. Delete associated grades first
            try (PreparedStatement psGrades = conn.prepareStatement(sqlGrades)) {
                psGrades.setInt(1, enrollmentId);
                psGrades.executeUpdate();
            }

            // 2. Delete the enrollment itself
            int rowsAffected;
            try (PreparedStatement psEnroll = conn.prepareStatement(sqlEnroll)) {
                psEnroll.setInt(1, enrollmentId); // Correctly uses enrollmentId
                rowsAffected = psEnroll.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return rowsAffected == 1;

        } catch (SQLException e) {
            // Let the exception propagate up to the service
            throw e;
        }
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

    public Settings getSettings() {
        // Default to false (off) if not found
        Settings settings = new Settings(false);
        String sql = "SELECT setting_value FROM settings WHERE setting_key = 'maintenance_mode'";

        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Parse the "true" or "false" string from the DB
                    boolean isEnabled = Boolean.parseBoolean(rs.getString("setting_value"));
                    settings.setMaintenanceMode(isEnabled);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching settings, defaulting to 'maintenance_mode' = false.");
        }
        return settings;
    }

    /**
     * Updates the maintenance mode setting in the database.
     * @param isEnabled The new state for maintenance mode.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateSettings(boolean isEnabled) {
        String sql = "UPDATE settings SET setting_value = ? WHERE setting_key = 'maintenance_mode'";

        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Convert the boolean to a string ("true" or "false") for the DB
            ps.setString(1, String.valueOf(isEnabled));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error updating settings.");
            return false;
        }
    }

    public boolean createStudentProfile(Student student) throws SQLException {
        String sql = "INSERT INTO students (user_id, roll_no, program, year) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, student.getUserId());
            ps.setString(2, student.getRollNo());
            ps.setString(3, student.getProgram());
            ps.setInt(4, student.getYear());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new instructor profile in the ERP DB.
     * This links to an existing user_id from the Auth DB.
     */
    public boolean createInstructorProfile(Instructor instructor) {
        String sql = "INSERT INTO instructors (user_id, name, email, department) VALUES (?, ?, ?, ?)";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, instructor.getUserId());
            ps.setString(2, instructor.getName());
            ps.setString(3, instructor.getEmail());
            ps.setString(4, instructor.getDepartment());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new course. (Uses the 'Course' domain object)
     */
    public boolean createCourse(Course course) {
        String sql = "INSERT INTO courses (code, title, credits) VALUES (?, ?, ?)";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getCode());
            ps.setString(2, course.getTitle());
            ps.setInt(3, course.getCredits());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates a new section. (Uses the 'Section' domain object)
     */
    public boolean createSection(Section section) {
        String sql = "INSERT INTO sections (course_id, instructor_id, day_time, room, capacity, semester, year) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, section.getCourseId());
            ps.setInt(2, section.getInstructorId());
            ps.setString(3, section.getDayTime());
            ps.setString(4, section.getRoom());
            ps.setInt(5, section.getCapacity());
            ps.setString(6, section.getSemester());
            ps.setInt(7, section.getYear());

            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
