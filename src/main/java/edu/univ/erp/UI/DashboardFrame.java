package edu.univ.erp.UI;

import edu.univ.erp.domain.UserAuth;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private final UserAuth user;

    public DashboardFrame(UserAuth user) {
        this.user = user;
        setTitle("University ERP - Dashboard (" + user.getRole() + ")");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel header = new JLabel(
                "Welcome, " + user.getUsername() + " [" + user.getRole() + "]",
                SwingConstants.CENTER
        );
        header.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);

        switch (user.getRole().toUpperCase()) {
            case "STUDENT" -> add(new StudentPanel(user), BorderLayout.CENTER);
            case "INSTRUCTOR" -> add(new InstructorPanel(user), BorderLayout.CENTER);
            case "ADMIN" -> add(new AdminPanel(user), BorderLayout.CENTER);
            default -> add(new JLabel("Unknown role: " + user.getRole(), SwingConstants.CENTER));
        }
    }
}
