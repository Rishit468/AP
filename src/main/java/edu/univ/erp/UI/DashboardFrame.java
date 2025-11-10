package edu.univ.erp.UI;

import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.service.AccessChecker; // ADDED

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final UserAuth user;
    private JLabel maintenanceBanner; // ADDED: Field for the banner

    public DashboardFrame(UserAuth user) {
        this.user = user;
        setTitle("University ERP - Dashboard (" + user.getRole() + ")");
        setSize(800, 600); // Increased size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Standard Header ---
        JLabel header = new JLabel(
                "Welcome, " + user.getUsername() + " [" + user.getRole() + "]",
                SwingConstants.CENTER
        );
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        // --- Role-Based Panel ---
        switch (user.getRole().toUpperCase()) {
            case "STUDENT" -> add(new StudentPanel(user), BorderLayout.CENTER);
            case "INSTRUCTOR" -> add(new InstructorPanel(user), BorderLayout.CENTER);
            case "ADMIN" -> add(new AdminPanel(user), BorderLayout.CENTER);
            default -> add(new JLabel("Unknown role: " + user.getRole(), SwingConstants.CENTER));
        }

        // ======================================================\
        // ⬇️ ADDED: Maintenance Mode Banner ⬇️
        // ======================================================\
        maintenanceBanner = new JLabel("Application is in MAINTENANCE MODE. All changes are disabled.", SwingConstants.CENTER);
        maintenanceBanner.setFont(new Font("SansSerif", Font.BOLD, 14));
        maintenanceBanner.setForeground(Color.WHITE);
        maintenanceBanner.setBackground(Color.RED);
        maintenanceBanner.setOpaque(true);
        add(maintenanceBanner, BorderLayout.SOUTH); // Add banner to the bottom

        // Call the update method to set initial visibility
        updateMaintenanceBanner();
    }

    /**
     * Public method to show/hide the maintenance banner.
     * This is called by the AdminPanel when the setting is changed.
     */
    public void updateMaintenanceBanner() {
        if (maintenanceBanner != null) {
            maintenanceBanner.setVisible(AccessChecker.isMaintenanceMode());
            revalidate();
            repaint();
        }
    }
}
