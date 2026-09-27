package com.iwfc.pattern;

import com.iwfc.model.Administrator;
import com.iwfc.model.Instructor;
import com.iwfc.model.Member;
import com.iwfc.model.User;

public final class UserFactory {
    private UserFactory() {}

    public static User createUser(String role, String userId, String name) {
        return switch (role.toLowerCase()) {
            case "administrator" -> new Administrator(userId, name);
            case "instructor" -> new Instructor(userId, name);
            case "member" -> new Member(userId, name);
            default -> throw new IllegalArgumentException("Unknown user role: " + role);
        };
    }
}
