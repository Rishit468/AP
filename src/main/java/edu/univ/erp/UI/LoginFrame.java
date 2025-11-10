package edu.univ.erp.UI;

// REMOVED: import edu.univ.erp.data.AuthDao;
import edu.univ.erp.service.AdminService;
import edu.univ.erp.service.AuthService; // ADDED
import edu.univ.erp.domain.UserAuth;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    // --- SERVICE LAYER ---
    private final AuthService authService = new AuthService(); // ADDED

    public LoginFrame() {
        setTitle("University ERP - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        loginButton = new JButton("Login");
        statusLabel = new JLabel(" ", SwingConstants.CENTER);

        add(formPanel, BorderLayout.CENTER);
        add(loginButton, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // -------------------------------------------------
        // ❌ MISTAKE: You were calling AuthDao here.
        // AuthDao authDao = new AuthDao();
        // UserAuth user = authDao.authenticate(username, password);
        // -------------------------------------------------

        // ✅ CORRECT: Call the AuthService
        UserAuth user = authService.authenticate(username, password);
        // -------------------------------------------------


        if (user != null) {
            statusLabel.setText("✅ Welcome, " + user.getUsername() + " (" + user.getRole() + ")");
            JOptionPane.showMessageDialog(this,
                    "Welcome " + user.getUsername() + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE);

            // Switch to role-based dashboard
            openDashboard(user);
        } else {
            statusLabel.setText("❌ Invalid credentials");
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openDashboard(UserAuth user) {
        dispose(); // close login window
        SwingUtilities.invokeLater(() -> new DashboardFrame(user).setVisible(true));
    }

    public static void main(String[] args) {
        System.out.println("Application starting up...");
        AdminService adminService = new AdminService();
        adminService.loadInitialSettings();
        // Here you would also initialize settings
        // e.g., AdminService.loadInitialSettings();
        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}
