package edu.univ.erp.service;

import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;

import java.util.List;

public class StudentService {

    private final ErpDao erpDao = new ErpDao();

    public Student getStudentProfile(int userId) {
        return erpDao.getStudentByUserId(userId);
    }

    public List<Course> getAllCourses() {
        return erpDao.getAllCourses();
    }

    public List<Enrollment> getEnrollments(int studentId) {
        return erpDao.getEnrollmentsByStudent(studentId);
    }

    public boolean enrollInSection(int studentId, int sectionId) {
        return erpDao.enrollStudent(studentId, sectionId);
    }

    public boolean dropSection(int studentId, int sectionId) {
        return erpDao.dropEnrollment(studentId, sectionId);
    }
}
