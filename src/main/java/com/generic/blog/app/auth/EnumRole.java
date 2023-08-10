package com.generic.blog.app.auth;

public enum EnumRole {
    ADMIN("admin"),
    USER("user");

    private final String role;

    public String getRole(){
        return this.role;
    }

    private EnumRole(String role) {
        this.role = role;
    }
}
