package com.iwfc.model;

public class Member extends User {
    public Member(String userId, String name) {
        super(userId, name);
    }

    @Override
    public String getRole() { return "Member"; }
}
