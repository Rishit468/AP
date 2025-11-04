package edu.univ.erp.data;

import edu.univ.erp.domain.UserAuth;

import java.sql.*;

public class AuthDao {

    /**
     * Fetch user record by username.
     * Does not perform password verification — handled in AuthService.
     */
    public UserAuth getUserByUsername(String username) {
        String sql = "SELECT user_id, username, pass_hash, role, status FROM users WHERE username = ?";
        try (Connection conn = DbPool.getAuthDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserAuth user = new UserAuth();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("pass_hash"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user: " + e.getMessage());
        }
        return null;
    }

    /**
     * Optionally used by admin — fetch by ID.
     */
    public UserAuth getUserById(int userId) {
        String sql = "SELECT user_id, username, pass_hash, role, status FROM users WHERE user_id = ?";
        try (Connection conn = DbPool.getAuthDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserAuth user = new UserAuth();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("pass_hash"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * (Optional) Create new user account.
     */
    public boolean createUser(String username, String hashedPassword, String role) {
        String sql = "INSERT INTO users (username, pass_hash, role, status) VALUES (?, ?, ?, 'ACTIVE')";
        try (Connection conn = DbPool.getAuthDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, role);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error creating user: " + e.getMessage());
        }
        return false;
    }

    /**
     * (Optional) Update account status (e.g., deactivate or lock).
     */
    public boolean updateUserStatus(int userId, String newStatus) {
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (Connection conn = DbPool.getAuthDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("Error updating user status: " + e.getMessage());
        }
        return false;
    }
}
