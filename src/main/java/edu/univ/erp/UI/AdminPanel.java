package edu.univ.erp.UI;

import edu.univ.erp.data.*;
import edu.univ.erp.domain.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.List;

public class AdminPanel extends JPanel {

    private final ErpDao erpDao = new ErpDao();
    private final UserAuth user;

    public AdminPanel(UserAuth user) {
        this.user = user;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Admin Dashboard - " + user.getUsername(), SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        // Main panel with multiple sections stacked vertically
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Add all data panels
        contentPanel.add(createSectionPanel("Users", getUsersTable()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createSectionPanel("Students", getStudentsTable()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createSectionPanel("Instructors", getInstructorsTable()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createSectionPanel("Courses", getCoursesTable()));
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(createSectionPanel("Sections", getSectionsTable()));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Utility method to wrap each JTable with a title and scroll pane
    private JPanel createSectionPanel(String title, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return panel;
    }

    // ==========================
    // USER TABLE
    // ==========================
    private JTable getUsersTable() {
        String[] cols = {"User ID", "Username", "Role", "Status"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        try (Connection conn = DbPool.getAuthDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT user_id, username, role, status FROM users");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("role"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JTable(model);
    }

    // ==========================
    // STUDENT TABLE
    // ==========================
    private JTable getStudentsTable() {
        String[] cols = {"Student ID", "User ID", "Roll No", "Program", "Year"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        String sql = "SELECT student_id, user_id, roll_no, program, year FROM students";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("student_id"),
                        rs.getInt("user_id"),
                        rs.getString("roll_no"),
                        rs.getString("program"),
                        rs.getInt("year")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JTable(model);
    }

    // ==========================
    // INSTRUCTOR TABLE
    // ==========================
    private JTable getInstructorsTable() {
        String[] cols = {"Instructor ID", "User ID", "Name", "Email", "Department"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        String sql = "SELECT instructor_id, user_id, name, email, department FROM instructors";
        try (Connection conn = DbPool.getErpDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("instructor_id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("department")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JTable(model);
    }

    // ==========================
    // COURSE TABLE
    // ==========================
    private JTable getCoursesTable() {
        List<Course> courses = erpDao.getAllCourses();
        String[] cols = {"Course ID", "Code", "Title", "Credits"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Course c : courses) {
            model.addRow(new Object[]{c.getCourseId(), c.getCode(), c.getTitle(), c.getCredits()});
        }
        return new JTable(model);
    }

    // ==========================
    // SECTION TABLE
    // ==========================
    private JTable getSectionsTable() {
        List<Section> sections = erpDao.getAllSections();
        String[] cols = {"Section ID", "Course ID", "Instructor ID", "Day/Time", "Room", "Capacity", "Semester", "Year", "Drop Deadline"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        for (Section s : sections) {
            model.addRow(new Object[]{
                    s.getSectionId(),
                    s.getCourseId(),
                    s.getInstructorId(),
                    s.getDayTime(),
                    s.getRoom(),
                    s.getCapacity(),
                    s.getSemester(),
                    s.getYear(),
                    s.getDropDeadline()
            });
        }
        return new JTable(model);
    }
}
