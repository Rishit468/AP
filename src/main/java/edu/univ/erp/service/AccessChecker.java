package edu.univ.erp.service;

import edu.univ.erp.domain.UserAuth;

public class AccessChecker {

    public static boolean isAdmin(UserAuth user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    public static boolean isInstructor(UserAuth user) {
        return user != null && "INSTRUCTOR".equalsIgnoreCase(user.getRole());
    }

    public static boolean isStudent(UserAuth user) {
        return user != null && "STUDENT".equalsIgnoreCase(user.getRole());
    }
}
