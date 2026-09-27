package com.iwfc.model;

public class Instructor extends User {
    public Instructor(String userId, String name) {
        super(userId, name);
    }

    @Override
    public String getRole() { return "Instructor"; }
}
