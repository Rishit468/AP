package edu.univ.erp.service;

import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;

import java.util.List;

public class InstructorService {

    private final ErpDao erpDao = new ErpDao();

    public Instructor getInstructorProfile(int userId) {
        return erpDao.getInstructorByUserId(userId);
    }

    public List<Course> getCoursesByInstructor(int instructorId) {
        return erpDao.getCoursesByInstructor(instructorId);
    }

    public List<Section> getSectionsByInstructor(int instructorId) {
        return erpDao.getSectionsByInstructor(instructorId);
    }

    public List<Student> getStudentsByInstructor(int instructorId) {
        return erpDao.getStudentsByInstructor(instructorId);
    }

    public List<Enrollment> getEnrollmentsBySection(int sectionId) {
        return erpDao.getEnrollmentsBySection(sectionId);
    }
}
