package edu.univ.erp.UI;

import edu.univ.erp.domain.*;
import edu.univ.erp.service.AccessChecker;
import edu.univ.erp.service.InstructorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class InstructorPanel extends JPanel {

    // --- Service Layer ---
    private final InstructorService instructorService;
    private final UserAuth user;
    private final Instructor instructor;

    // --- UI Components ---
    private JComboBox<Section> sectionComboBox;
    private JTable rosterTable;
    private DefaultTableModel rosterTableModel;
    private JButton saveGradesButton;
    private JPanel gradeEntryPanel;

    // Data storage
    private List<Section> sectionsTaught;
    private List<Enrollment> currentRoster;
    private Map<Integer, Map<String, Double>> gradeMap;

    // Column indices for the table (must match the order)
    private static final int COL_ENROLL_ID = 0;
    private static final int COL_ROLL_NO = 1;
    private static final int COL_NAME = 2;
    private static final int COL_MIDTERM = 3;
    private static final int COL_FINAL = 4;

    public InstructorPanel(UserAuth user) {
        this.user = user;
        this.instructorService = new InstructorService();
        this.instructor = instructorService.getInstructorProfile(user.getUserId());

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (instructor == null) {
            add(new JLabel("Instructor record not found."), BorderLayout.CENTER);
            return;
        }

        // 1. Top Panel (Profile Info & Section Selector)
        add(createTopPanel(), BorderLayout.NORTH);

        // 2. Center Panel (Grade Entry Table)
        add(createGradeEntryPanel(), BorderLayout.CENTER);

        // 3. Load initial data
        loadSections();
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));

        // Profile Info
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("Instructor: " + instructor.getName()));
        infoPanel.add(new JLabel("| Department: " + instructor.getDepartment()));

        // Section Selector
        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectorPanel.add(new JLabel("Select Section:"));
        sectionComboBox = new JComboBox<>();

        selectorPanel.add(sectionComboBox);

        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(selectorPanel, BorderLayout.CENTER);

        // Add listener to the ComboBox
        sectionComboBox.addActionListener(e -> {
            Section selectedSection = (Section) sectionComboBox.getSelectedItem();
            if (selectedSection != null) {
                loadRosterForSection(selectedSection.getSectionId());
            }
        });

        return topPanel;
    }

    private JPanel createGradeEntryPanel() {
        gradeEntryPanel = new JPanel(new BorderLayout(10, 10));
        gradeEntryPanel.setBorder(BorderFactory.createTitledBorder("Grade Roster"));

        // Table Model
        String[] columnNames = {"Enroll ID", "Roll No", "Student Name", "Midterm", "Final"};
        rosterTableModel = new DefaultTableModel(columnNames, 0) {
            // Make only grade columns editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == COL_MIDTERM || column == COL_FINAL;
            }
        };

        rosterTable = new JTable(rosterTableModel);
        // Hide the Enrollment ID column (it's needed for saving but not for display)
        rosterTable.getColumnModel().getColumn(COL_ENROLL_ID).setMinWidth(0);
        rosterTable.getColumnModel().getColumn(COL_ENROLL_ID).setMaxWidth(0);
        rosterTable.getColumnModel().getColumn(COL_ENROLL_ID).setWidth(0);

        gradeEntryPanel.add(new JScrollPane(rosterTable), BorderLayout.CENTER);

        // Save Button
        saveGradesButton = new JButton("Save All Grades for This Section");
        saveGradesButton.setBackground(new Color(60, 179, 113)); // Green
        saveGradesButton.setForeground(Color.WHITE);
        saveGradesButton.setEnabled(false); // Disabled until a section is loaded

        saveGradesButton.addActionListener(e -> handleSaveGrades());

        gradeEntryPanel.add(saveGradesButton, BorderLayout.SOUTH);
        return gradeEntryPanel;
    }

    /**
     * Loads the instructor's sections into the JComboBox.
     */
    private void loadSections() {
        sectionsTaught = instructorService.getSectionsByInstructor(instructor.getInstructorId());

        // Use a custom renderer to show Course ID and Semester
        sectionComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Section) {
                    Section s = (Section) value;
                    setText(String.format("Sec %d (Course %d) - %s %d", s.getSectionId(), s.getCourseId(), s.getSemester(), s.getYear()));
                } else if (value == null) {
                    setText("Select a section...");
                }
                return this;
            }
        });

        sectionComboBox.addItem(null); // Add a "Select" prompt
        for (Section s : sectionsTaught) {
            sectionComboBox.addItem(s);
        }
    }

    /**
     * Loads the student roster and grades for the selected section.
     */
    private void loadRosterForSection(int sectionId) {
        // Stop any active cell editing before reloading
        stopCellEditing();

        // Fetch data from service
        currentRoster = instructorService.getEnrollmentsBySection(sectionId);
        gradeMap = instructorService.getGradesForSection(sectionId);

        // Clear old table data
        rosterTableModel.setRowCount(0);

        // Populate table
        for (Enrollment e : currentRoster) {
            int enrollmentId = e.getEnrollmentId();
            // Get grades for this student from the map
            Map<String, Double> studentGrades = gradeMap.getOrDefault(enrollmentId, new HashMap<>());

            rosterTableModel.addRow(new Object[]{
                    enrollmentId,
                    e.getRollNo(),
                    e.getStudentName(),
                    studentGrades.get("Midterm"), // Will be null if not present
                    studentGrades.get("Final")   // Will be null if not present
            });
        }

        saveGradesButton.setEnabled(true);
    }

    /**
     * Handles the "Save Grades" button click.
     */
    private void handleSaveGrades() {
        // Stop editing to apply the last change
        stopCellEditing();

        // 1. Check for Maintenance Mode
        if (AccessChecker.isMaintenanceMode()) {
            JOptionPane.showMessageDialog(this,
                    "The system is in maintenance mode. Changes are disabled.",
                    "Save Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Read all data from the JTable
        List<Grade> gradesToSave = new ArrayList<>();
        int rowCount = rosterTableModel.getRowCount();

        for (int i = 0; i < rowCount; i++) {
            int enrollmentId = (int) rosterTableModel.getValueAt(i, COL_ENROLL_ID);

            // Get Midterm grade
            Double midtermScore = parseGrade(rosterTableModel.getValueAt(i, COL_MIDTERM));
            gradesToSave.add(new Grade(0, enrollmentId, "Midterm", midtermScore));

            // Get Final grade
            Double finalScore = parseGrade(rosterTableModel.getValueAt(i, COL_FINAL));
            gradesToSave.add(new Grade(0, enrollmentId, "Final", finalScore));
        }

        // 3. Call the service to save
        if (instructorService.saveGrades(gradesToSave)) {
            JOptionPane.showMessageDialog(this,
                    "Grades saved successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            // Reload data to confirm
            Section selectedSection = (Section) sectionComboBox.getSelectedItem();
            if (selectedSection != null) {
                loadRosterForSection(selectedSection.getSectionId());
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to save grades due to a database error.",
                    "Save Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Safely parses a grade from the table object.
     * Returns null if empty or invalid.
     */
    private Double parseGrade(Object value) {
        if (value == null) return null;
        try {
            return Double.parseDouble(value.toString().trim());
        } catch (NumberFormatException e) {
            return null; // Invalid numbers are treated as null (empty)
        }
    }

    /**
     * Helper to stop cell editing if active.
     */
    private void stopCellEditing() {
        if (rosterTable.isEditing()) {
            TableCellEditor editor = rosterTable.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
        }
    }
}
