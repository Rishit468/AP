package edu.univ.erp.data;

import edu.univ.erp.domain.UserAuth;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class AuthDao {
    public UserAuth authenticate(String username, String plainPassword) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DbPool.getAuthDataSource().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("pass_hash");
                String status = rs.getString("status");

                // optional: skip locked users
                if ("LOCKED".equalsIgnoreCase(status)) {
                    System.out.println("User account is locked.");
                    return null;
                }

                if (BCrypt.checkpw(plainPassword, hash)) {
                    UserAuth u = new UserAuth();
                    u.setUserId(rs.getInt("user_id"));
                    u.setUsername(rs.getString("username"));
                    u.setRole(rs.getString("role"));
                    u.setStatus(status); // ✅ now matches UserAuth.java
                    return u;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
