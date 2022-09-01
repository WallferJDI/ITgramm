package com.wallferjdi.itgramm.payload.response;

public class InvalidLoginResponse {
    private String username;
    private String password;

    public InvalidLoginResponse() {
        this.username = "Invalid username";
        this.password = "Invalid password";
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
