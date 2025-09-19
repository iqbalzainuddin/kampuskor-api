package com.kampuskor.restservice.features.User.dto;

public class LoginResponse {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }
    
    public String getToken() {
        return token;
    }
}
