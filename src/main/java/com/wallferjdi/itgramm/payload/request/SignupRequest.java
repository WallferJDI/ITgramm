package com.wallferjdi.itgramm.payload.request;

import com.wallferjdi.itgramm.annotations.PasswordMatches;
import com.wallferjdi.itgramm.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "Check email format")
    @NotBlank(message = "This field is required")
    @ValidEmail
    private String email;
    @NotEmpty(message = "This field is required")
    private String firstname;
    @NotEmpty(message = "This field is required")
    private String lastname;
    @NotEmpty(message = "This field is required")
    private String username;
    @NotEmpty(message = "This field is required")
    @Size(min = 6)
    private String password;

    private String confirmPassword;


}
