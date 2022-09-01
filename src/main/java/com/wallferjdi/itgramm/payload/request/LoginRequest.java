package com.wallferjdi.itgramm.payload.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {

    @NotEmpty(message = " username cannot be null")
    private String username;
    @NotEmpty(message = " password cannot be null")
    private String password;
}
