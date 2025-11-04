package edu.univ.erp.service;

import edu.univ.erp.data.AuthDao;
import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;

import java.util.List;

public class AdminService {

    private final AuthDao authDao = new AuthDao();
    private final ErpDao erpDao = new ErpDao();

    public List<Course> getAllCourses() {
        return erpDao.getAllCourses();
    }

    public boolean createUser(String username, String plainPassword, String role) {
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
}
