package edu.univ.erp.test;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;
import java.util.List;

public class DaoTest {
    public static void main(String[] args) {
        AuthDao authDao = new AuthDao();
        ErpDao erpDao = new ErpDao();

        // 1️⃣ Test authentication
        System.out.println("=== AUTH TEST ===");
        UserAuth user = authDao.authenticate("stu1", "Stu1@123");


        if (user != null) {
            System.out.println("Login successful: " + user);
        } else {
            System.out.println("Login failed!");
        }

        // 2️⃣ Test student lookup
        System.out.println("\n=== STUDENT TEST ===");
        Student s = erpDao.getStudentByUserId(3);
        System.out.println(s);

        // 3️⃣ Test courses
        System.out.println("\n=== COURSES ===");
        List<Course> courses = erpDao.getAllCourses();
        for (Course c : courses) System.out.println(c);

        // 4️⃣ Test enrollments
        System.out.println("\n=== ENROLLMENTS ===");
        List<Enrollment> enrollments = erpDao.getEnrollmentsByStudent(3);
        for (Enrollment e : enrollments) System.out.println(e);

        // 5️⃣ Test grades
        System.out.println("\n=== GRADES ===");
        List<Grade> grades = erpDao.getGradesByEnrollment(1);
        for (Grade g : grades) System.out.println(g);
    }
}
