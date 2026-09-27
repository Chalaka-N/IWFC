package com.iwfc.model;

public class Administrator extends User {
    public Administrator(String userId, String name) {
        super(userId, name);
    }

    @Override
    public String getRole() { return "Administrator"; }
}
