package edu.univ.erp.UI;

import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {

    private final ErpDao erpDao = new ErpDao();
    private final UserAuth user;

    public StudentPanel(UserAuth user) {
        this.user = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Student s = erpDao.getStudentByUserId(user.getUserId());
        if (s == null) {
            add(new JLabel("No student record found."), BorderLayout.CENTER);
            return;
        }

        JPanel profilePanel = new JPanel(new GridLayout(0, 1));
        profilePanel.add(new JLabel("Roll No: " + s.getRollNo()));
        profilePanel.add(new JLabel("Program: " + s.getProgram()));
        profilePanel.add(new JLabel("Year: " + s.getYear()));

        add(profilePanel, BorderLayout.NORTH);

        // Courses table
        List<Course> courses = erpDao.getAllCourses();
        String[] courseCols = {"ID", "Code", "Title", "Credits"};
        DefaultTableModel courseModel = new DefaultTableModel(courseCols, 0);
        for (Course c : courses)
            courseModel.addRow(new Object[]{c.getCourseId(), c.getCode(), c.getTitle(), c.getCredits()});
        JTable courseTable = new JTable(courseModel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Available Courses:"), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Enrollment info
        List<Enrollment> enrollments = erpDao.getEnrollmentsByStudent(s.getUserId());
        String[] enrollCols = {"Enrollment ID", "Section ID", "Status"};
        DefaultTableModel enrollModel = new DefaultTableModel(enrollCols, 0);
        for (Enrollment e : enrollments)
            enrollModel.addRow(new Object[]{e.getEnrollmentId(), e.getSectionId(), e.getStatus()});

        JTable enrollTable = new JTable(enrollModel);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Your Enrollments:"), BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(enrollTable), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
}
