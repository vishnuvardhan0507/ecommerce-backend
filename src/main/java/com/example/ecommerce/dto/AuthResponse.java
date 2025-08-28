package com.example.ecommerce.dto;

import com.example.ecommerce.model.Role;

public class AuthResponse {
    private String token;
    private String username;
    private Long userId;
    private Role role;
    public AuthResponse(String token, String username, Long userId,Role role) {
        this.token = token;
        this.username = username;
        this.userId = userId;
        this.role=role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
