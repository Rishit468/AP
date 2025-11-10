package edu.univ.erp.service;

import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*; // Import all domain classes
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentService {

    private final ErpDao erpDao = new ErpDao();

    // Caches for fast lookups in the UI table
    // This prevents making N+1 database queries inside a loop
    private Map<Integer, Course> courseCache;
    private Map<Integer, Instructor> instructorCache;

    /**
     * This enum provides clear, user-friendly responses for the UI.
     */
    public enum EnrollmentResponse {
        SUCCESS("Enrolled successfully!"),
        DROPPED("Dropped successfully!"),
        MAINTENANCE_MODE("Changes cannot be made while in maintenance mode."),
        SECTION_FULL("This section is full."),
        ALREADY_ENROLLED("You are already enrolled in this section."),
        DATABASE_ERROR("An unexpected database error occurred."),
        FAILED("The operation failed.");

        public final String message;
        EnrollmentResponse(String message) { this.message = message; }
    }

    /**
     * Constructor: Pre-loads all courses and instructors into maps.
     */
    public StudentService() {
        try {
            this.courseCache = erpDao.getAllCourses().stream()
                    .collect(Collectors.toMap(Course::getCourseId, c -> c));

            // This maps by instructor_id, which is what the Section table uses
            this.instructorCache = erpDao.getAllInstructors().stream()
                    .collect(Collectors.toMap(Instructor::getInstructorId, i -> i));
        } catch (Exception e) {
            System.err.println("Error pre-caching courses/instructors: " + e.getMessage());
            this.courseCache = Collections.emptyMap();
            this.instructorCache = Collections.emptyMap();
        }
    }

    /**
     * Gets the student's personal profile (Roll No, Program, etc.).
     */
    public Student getStudentProfile(int userId) {
        return erpDao.getStudentByUserId(userId);
    }

    /**
     * Gets all available sections for the "Available Sections" table.
     */
    public List<Section> getAllAvailableSections() {
        return erpDao.getAllSections();
    }

    /**
     * Helper method for the UI to get a Course by ID from the cache.
     */
    public Course getCourseById(int courseId) {
        return courseCache.get(courseId);
    }

    /**
     * Helper method for the UI to get an Instructor by ID from the cache.
     */
    public Instructor getInstructorByInstructorId(int instructorId) {
        return instructorCache.get(instructorId);
    }

    /**
     * Gets the student's currently enrolled classes (detailed view).
     */
    public List<Enrollment> getDetailedEnrollmentsByStudent(int studentId) {
        return erpDao.getEnrollmentsByStudent(studentId);
    }

    /**
     * Business logic for enrolling a student in a section.
     */
    public EnrollmentResponse enrollInSection(int studentId, int sectionId) {
        // 1. Check Maintenance Mode (using the service we built earlier)
        if (AccessChecker.isMaintenanceMode()) {
            return EnrollmentResponse.MAINTENANCE_MODE;
        }

        // 2. Check if section is full
        Section section = erpDao.getSectionById(sectionId);
        if (section == null) {
            return EnrollmentResponse.FAILED; // Section doesn't exist
        }

        int currentEnrollment = erpDao.getEnrollmentCountBySection(sectionId);
        if (currentEnrollment >= section.getCapacity()) {
            return EnrollmentResponse.SECTION_FULL;
        }

        // 3. Try to enroll (DAO will handle duplicate check)
        try {
            if (erpDao.enrollStudent(studentId, sectionId)) {
                return EnrollmentResponse.SUCCESS;
            } else {
                return EnrollmentResponse.FAILED;
            }
        } catch (SQLException e) {
            // Check for duplicate key violation (SQL state '23' series)
            if (e.getSQLState().startsWith("23")) {
                return EnrollmentResponse.ALREADY_ENROLLED;
            }
            e.printStackTrace();
            return EnrollmentResponse.DATABASE_ERROR;
        }
    }

    /**
     * Business logic for dropping a student from a section.
     */
    public EnrollmentResponse dropEnrollment(int enrollmentId) {
        // 1. Check Maintenance Mode
        if (AccessChecker.isMaintenanceMode()) {
            return EnrollmentResponse.MAINTENANCE_MODE;
        }

        // 2. Try to drop
        try {
            if (erpDao.dropEnrollment(enrollmentId)) {
                return EnrollmentResponse.DROPPED;
            } else {
                return EnrollmentResponse.FAILED;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return EnrollmentResponse.DATABASE_ERROR;
        }
    }
}

