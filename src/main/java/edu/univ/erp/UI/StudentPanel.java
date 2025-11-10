package edu.univ.erp.UI;

// ❌ REMOVED: import edu.univ.erp.data.ErpDao;
// ✅ ADDED:
import edu.univ.erp.service.StudentService;
import edu.univ.erp.domain.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {

    // --- Service Layer ---
    // ❌ REMOVED: private final ErpDao erpDao = new ErpDao();
    // ✅ ADDED:
    private final StudentService studentService;

    private final UserAuth user;
    private final Student studentProfile;

    // Table models to be updated
    private DefaultTableModel availableSectionsModel;
    private DefaultTableModel enrolledSectionsModel;
    private JTable availableSectionsTable;
    private JTable enrolledSectionsTable;

    /**
     * This is the constructor that DashboardFrame.java is looking for.
     */
    public StudentPanel(UserAuth user) {
        this.user = user;
        this.studentService = new StudentService(); // Initialize the service
        this.studentProfile = studentService.getStudentProfile(user.getUserId());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Check if student profile exists
        if (studentProfile == null) {
            add(new JLabel("No student record found for user ID: " + user.getUserId()), BorderLayout.CENTER);
            return;
        }

        // 2. Build the UI
        add(createProfilePanel(), BorderLayout.NORTH);

        // Use a split pane for the two tables
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                createAvailableSectionsPanel(),
                createEnrolledSectionsPanel()
        );
        splitPane.setResizeWeight(0.5); // Divide the space 50/50
        add(splitPane, BorderLayout.CENTER);

        // 3. Load initial data
        refreshTables();
    }

    /**
     * Creates the top panel showing the student's personal info.
     */
    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBorder(BorderFactory.createTitledBorder("Student Profile"));
        profilePanel.add(new JLabel("Name: " + user.getUsername()));
        profilePanel.add(new JLabel(" | Roll No: " + studentProfile.getRollNo()));
        profilePanel.add(new JLabel(" | Program: " + studentProfile.getProgram()));
        profilePanel.add(new JLabel(" | Year: " + studentProfile.getYear()));
        return profilePanel;
    }

    /**
     * Creates the panel with the "Available Sections" table and "Enroll" button.
     */
    private JPanel createAvailableSectionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Available Sections"));

        String[] cols = {"Sec. ID", "Code", "Title", "Instructor", "Day/Time", "Room", "Cap."};
        availableSectionsModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        availableSectionsTable = new JTable(availableSectionsModel);
        availableSectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(availableSectionsTable), BorderLayout.CENTER);

        JButton enrollButton = new JButton("Enroll in Selected Section");
        enrollButton.setBackground(new Color(60, 179, 113)); // Green
        enrollButton.setForeground(Color.WHITE);
        enrollButton.addActionListener(e -> handleEnroll());

        panel.add(enrollButton, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates the panel with the "My Enrollments" table and "Drop" button.
     */
    private JPanel createEnrolledSectionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("My Enrollments"));

        String[] cols = {"Enroll ID", "Code", "Title", "Status", "Day/Time", "Room", "Instructor"};
        enrolledSectionsModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        enrolledSectionsTable = new JTable(enrolledSectionsModel);
        enrolledSectionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panel.add(new JScrollPane(enrolledSectionsTable), BorderLayout.CENTER);

        JButton dropButton = new JButton("Drop Selected Section");
        dropButton.setBackground(new Color(220, 20, 60)); // Red
        dropButton.setForeground(Color.WHITE);
        dropButton.addActionListener(e -> handleDrop());

        panel.add(dropButton, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Action handler for the "Enroll" button.
     */
    private void handleEnroll() {
        int selectedRow = availableSectionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a section to enroll in.", "No Section Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the section ID from the model (column 0)
        int sectionId = (int) availableSectionsModel.getValueAt(selectedRow, 0);

        // Call the service
        StudentService.EnrollmentResponse response = studentService.enrollInSection(studentProfile.getUserId(), sectionId);

        // Show the result
        if (response == StudentService.EnrollmentResponse.SUCCESS) {
            JOptionPane.showMessageDialog(this, response.message, "Success", JOptionPane.INFORMATION_MESSAGE);
            refreshTables(); // Reload data
        } else {
            JOptionPane.showMessageDialog(this, response.message, "Enrollment Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Action handler for the "Drop" button.
     */
    private void handleDrop() {
        int selectedRow = enrolledSectionsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a section to drop.", "No Section Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Get the enrollment ID from the model (column 0)
        int enrollmentId = (int) enrolledSectionsModel.getValueAt(selectedRow, 0);
        String courseCode = (String) enrolledSectionsModel.getValueAt(selectedRow, 1);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to drop " + courseCode + "?",
                "Confirm Drop",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            // Call the service
            StudentService.EnrollmentResponse response = studentService.dropEnrollment(enrollmentId);

            // Show the result
            if (response == StudentService.EnrollmentResponse.DROPPED) {
                JOptionPane.showMessageDialog(this, response.message, "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshTables(); // Reload data
            } else {
                JOptionPane.showMessageDialog(this, response.message, "Drop Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Refreshes both tables with the latest data from the database.
     */
    private void refreshTables() {
        loadAvailableSections();
        loadEnrolledSections();
    }

    /**
     * Loads all available sections from the service and populates the top table.
     */
    private void loadAvailableSections() {
        // Clear old data
        availableSectionsModel.setRowCount(0);

        List<Section> sections = studentService.getAllAvailableSections();
        for (Section s : sections) {
            // Use the service's cache to get human-readable names
            Course c = studentService.getCourseById(s.getCourseId());
            Instructor i = studentService.getInstructorByInstructorId(s.getInstructorId());

            String courseCode = (c != null) ? c.getCode() : "N/A";
            String courseTitle = (c != null) ? c.getTitle() : "N/A";
            String instName = (i != null) ? i.getName() : "TBD";
            String capacity = s.getCapacity() + ""; // Simple capacity display

            availableSectionsModel.addRow(new Object[]{
                    s.getSectionId(),
                    courseCode,
                    courseTitle,
                    instName,
                    s.getDayTime(),
                    s.getRoom(),
                    capacity
            });
        }
    }

    /**
     * Loads the student's current enrollments from the service and populates the bottom table.
     */
    private void loadEnrolledSections() {
        // Clear old data
        enrolledSectionsModel.setRowCount(0);

        List<Enrollment> enrollments = studentService.getDetailedEnrollmentsByStudent(studentProfile.getUserId());

        for (Enrollment e : enrollments) {
            // The Enrollment object is already populated with joined data
            enrolledSectionsModel.addRow(new Object[]{
                    e.getEnrollmentId(),
                    e.getCourseCode(),
                    e.getCourseTitle(),
                    e.getStatus(),
                    e.getDayTime(),
                    e.getRoom(),
                    e.getInstructorName() != null ? e.getInstructorName() : "TBD"
            });
        }
    }
}

