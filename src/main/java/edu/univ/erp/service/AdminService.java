package edu.univ.erp.service;

import edu.univ.erp.data.AuthDao;
import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;
import edu.univ.erp.util.PasswordUtil;

import java.sql.SQLException;
import java.util.List;

public class AdminService {

    private final AuthDao authDao = new AuthDao();
    private final ErpDao erpDao = new ErpDao();

    public List<Course> getAllCourses() {
        return erpDao.getAllCourses();
    }

    public UserAuth createUser(String username, String plainPassword, String role) {
        String hashed = edu.univ.erp.util.PasswordUtil.hashPassword(plainPassword);
        return authDao.createUser(username, hashed, role);
    }

    public boolean updateUserStatus(int userId, String newStatus) {
        return authDao.updateUserStatus(userId, newStatus);
    }

    public Course getCourseById(int id) {
        return erpDao.getCourseById(id);
    }

    public List<Section> getAllSections() {
        return erpDao.getAllSections();
    }

    public Instructor getInstructorByUserId(int userId) {
        return erpDao.getInstructorByUserId(userId);
    }

    public Student getStudentByUserId(int userId) {
        return erpDao.getStudentByUserId(userId);
    }

    public List<UserAuth> getAllAuthUsers() {
        // Add error handling in a real app
        return authDao.getAllUsers();
    }

    /**
     * Gets all student profiles from the ERP DB.
     * (For AdminPanel)
     */
    public List<Student> getAllStudents() {
        // Add error handling in a real app
        return erpDao.getAllStudents(); // This method already existed in ErpDao
    }

    /**
     * Gets all instructor profiles from the ERP DB.
     * (For AdminPanel)
     */
    public List<Instructor> getAllInstructors() {
        // Add error handling in a real app
        return erpDao.getAllInstructors(); // This calls the method we just added to ErpDao
    }

    public void loadInitialSettings() {
        System.out.println("[Service] Loading initial settings...");
        Settings currentSettings = erpDao.getSettings();
        AccessChecker.loadInitialSettings(currentSettings);
        System.out.println("[Service] Maintenance mode loaded as: " + AccessChecker.isMaintenanceMode());
    }

    /**
     * Gets the current settings from the DB.
     * (Used by AdminPanel to set checkbox state)
     */
    public Settings getSettings() {
        return erpDao.getSettings();
    }

    /**
     * Updates maintenance mode in the DB and in AccessChecker.
     * (Called by AdminPanel's checkbox)
     * @param isEnabled The new state.
     * @return true on success.
     */
    public boolean updateMaintenanceMode(boolean isEnabled) {
        boolean dbSuccess = erpDao.updateSettings(isEnabled);
        if (dbSuccess) {
            // Also update the live in-memory state
            AccessChecker.setMaintenanceMode(isEnabled);
            System.out.println("[Service] Maintenance mode updated to: " + isEnabled);
        } else {
            System.err.println("[Service] Failed to update maintenance mode in DB.");
        }
        return dbSuccess;
    }
    public UserAuth createNewUser(String username, String plainPassword, String role) {
        String hashed = PasswordUtil.hashPassword(plainPassword);
        // This now returns the UserAuth object from AuthDao
        return authDao.createUser(username, hashed, role);
    }

    /**
     * Creates a new student profile in the ERP DB.
     */
    public boolean createStudentProfile(Student student) throws SQLException {
        // Add validation logic here
        return erpDao.createStudentProfile(student);
    }

    /**
     * Creates a new instructor profile in the ERP DB.
     */
    public boolean createInstructorProfile(Instructor instructor) {
        // Add validation logic here
        return erpDao.createInstructorProfile(instructor);
    }

    /**
     * Creates a new course.
     */
    public boolean createNewCourse(Course course) {
        // Add validation logic here
        return erpDao.createCourse(course);
    }

    /**
     * Creates a new section.
     */
    public boolean createNewSection(Section section) {
        // Add validation logic here
        return erpDao.createSection(section);
    }

}
