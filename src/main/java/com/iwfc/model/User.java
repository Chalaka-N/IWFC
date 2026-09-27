package com.iwfc.model;

public abstract class User {
    private final String userId;
    private String name;

    protected User(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public abstract String getRole();

    public String getProfileSummary() {
        return userId + " - " + name + " (" + getRole() + ")";
    }
}
