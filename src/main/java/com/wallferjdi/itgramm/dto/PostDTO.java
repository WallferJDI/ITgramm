package com.wallferjdi.itgramm.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostDTO {

    private Long id;
    private String title;
    private String caption;
    private String location;
    private Integer likes;
    private String username;
    private Set<String> userLiked;
}
