package com.wallferjdi.itgramm.dto;

import lombok.Data;

import javax.persistence.Column;

@Data
public class UserDTO {

    private long id;
    private String name;
    private String username;
    private String lastname;
    private String bio;
}
