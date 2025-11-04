package edu.univ.erp.UI;

import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InstructorPanel extends JPanel {

    private final ErpDao erpDao = new ErpDao();
    private final UserAuth user;

    public InstructorPanel(UserAuth user) {
        this.user = user;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Instructor instructor = erpDao.getInstructorByUserId(user.getUserId());
        if (instructor == null) {
            add(new JLabel("Instructor record not found."), BorderLayout.CENTER);
            return;
        }

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.add(new JLabel("Name: " + instructor.getName()));
        infoPanel.add(new JLabel("Email: " + instructor.getEmail()));
        infoPanel.add(new JLabel("Department: " + instructor.getDepartment()));

        add(infoPanel, BorderLayout.NORTH);

        // Sections taught by instructor
        List<Section> sections = erpDao.getSectionsByInstructor(instructor.getInstructorId());
        String[] secCols = {"Section ID", "Course ID", "Day/Time", "Room", "Semester", "Year"};
        DefaultTableModel secModel = new DefaultTableModel(secCols, 0);
        for (Section s : sections)
            secModel.addRow(new Object[]{
                    s.getSectionId(),
                    s.getCourseId(),
                    s.getDayTime(),
                    s.getRoom(),
                    s.getSemester(),
                    s.getYear()
            });
        JTable secTable = new JTable(secModel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JLabel("Sections Taught:"), BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(secTable), BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Students in first section (optional demo)
        if (!sections.isEmpty()) {
            int sectionId = sections.get(0).getSectionId();
            List<Enrollment> enrollments = erpDao.getEnrollmentsBySection(sectionId);

            String[] enrollCols = {"Enrollment ID", "Student ID", "Status"};
            DefaultTableModel enrollModel = new DefaultTableModel(enrollCols, 0);
            for (Enrollment e : enrollments)
                enrollModel.addRow(new Object[]{e.getEnrollmentId(), e.getStudentId(), e.getStatus()});

            JTable enrollTable = new JTable(enrollModel);
            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(new JLabel("Students in Section " + sectionId + ":"), BorderLayout.NORTH);
            bottomPanel.add(new JScrollPane(enrollTable), BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);
        }
    }
}
