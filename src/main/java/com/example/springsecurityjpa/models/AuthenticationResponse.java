package com.example.springsecurityjpa.models;

import lombok.Builder;

@Builder
public class AuthenticationResponse {
    private String jwtToken;
    private String username;

    public void AuthenticationRequest(){}

    public void AuthenticationRequest(String token, String username) {
        this.jwtToken = token;
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
