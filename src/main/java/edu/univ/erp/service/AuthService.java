package edu.univ.erp.service;

import edu.univ.erp.data.AuthDao;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.util.PasswordUtil;

public class AuthService {

    private final AuthDao authDao = new AuthDao();

    /**
     * Authenticates user by verifying credentials.
     */
    public UserAuth authenticate(String username, String plainPassword) {
        // --- ⬇️ DEBUGGING LINES ⬇️ ---
        System.out.println("DEBUG: Attempting login for: " + username);
        UserAuth user = authDao.getUserByUsername(username);

        if (user == null) {
            System.out.println("DEBUG: User not found in database.");
            return null;
        }

        System.out.println("DEBUG: Hash from DB: " + user.getPasswordHash());
        System.out.println("DEBUG: Plain pass: " + plainPassword);
        // --- ⬆️ END DEBUGGING ⬆️ ---

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            System.out.println("User is inactive or locked.");
            return null;
        }

        boolean match = PasswordUtil.verifyPassword(plainPassword, user.getPasswordHash());

        // --- ⬇️ DEBUGGING LINES ⬇️ ---
        System.out.println("DEBUG: Password match result: " + match);
        // --- ⬆️ END DEBUGGING ⬆️ ---

        return match ? user : null;
    }


    /**
     * Creates a new user account.
     */
    public UserAuth register(String username, String plainPassword, String role) {
        String hashed = PasswordUtil.hashPassword(plainPassword);
        return authDao.createUser(username, hashed, role);
    }

    /**
     * Locks or unlocks user accounts.
     */
    public boolean updateStatus(int userId, String status) {
        return authDao.updateUserStatus(userId, status);
    }
}
