package edu.univ.erp.service;

import edu.univ.erp.domain.Settings;
import edu.univ.erp.domain.UserAuth;
import java.util.concurrent.atomic.AtomicBoolean; // Added

public class AccessChecker {

    // Use AtomicBoolean for thread-safe updates from the UI
    private static final AtomicBoolean maintenanceMode = new AtomicBoolean(false);

    /**
     * Called by AdminService to load settings at startup.
     */
    public static void loadInitialSettings(Settings settings) {
        if (settings != null) {
            maintenanceMode.set(settings.isMaintenanceMode());
        }
    }

    /**
     * Called by AdminService when the admin toggles the mode.
     */
    public static void setMaintenanceMode(boolean isEnabled) {
        maintenanceMode.set(isEnabled);
    }

    /**
     * Checks if the application is currently in maintenance mode.
     * @return true if in maintenance mode, false otherwise.
     */
    public static boolean isMaintenanceMode() {
        return maintenanceMode.get();
    }

    public static boolean isAdmin(UserAuth user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    public static boolean isInstructor(UserAuth user) {
        return user != null && "INSTRUCTOR".equalsIgnoreCase(user.getRole());
    }

    public static boolean isStudent(UserAuth user) {
        return user != null && "STUDENT".equalsIgnoreCase(user.getRole());
    }

    /**
     * Checks if a user is allowed to make changes.
     * Admins can always make changes.
     * Students/Instructors cannot make changes if maintenance mode is ON.
     * @param user The user performing the action.
     * @return true if changes are allowed, false otherwise.
     */
    public static boolean canMakeChanges(UserAuth user) {
        if (isAdmin(user)) {
            return true; // Admins are exempt
        }
        // For Students/Instructors, return true only if maintenance mode is OFF
        return !isMaintenanceMode();
    }
}
