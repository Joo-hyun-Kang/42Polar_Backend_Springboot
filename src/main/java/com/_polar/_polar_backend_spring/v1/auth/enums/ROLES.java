package com._polar._polar_backend_spring.v1.auth.enums;

public enum ROLES {
    MENTOR("mentor"),
    CADET("cadet"),
    BOCAL("bocal");

    private final String role;

    ROLES(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return role;
    }
}

