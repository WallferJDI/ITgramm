package com.wallferjdi.itgramm.entity;

import lombok.Data;
import org.hibernate.annotations.ManyToAny;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String caption;
    private String location;
    private Integer likes;

    @Column
    @ElementCollection(targetClass = String.class)
    private Set<String> linkedUsers = new HashSet<>();

    @ManyToOne(fetch =  FetchType.LAZY)
    private User user;
    @OneToMany(cascade = CascadeType.REFRESH,fetch = FetchType.EAGER,mappedBy ="post",orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createdTime;

    @PrePersist // implements after deploy in database
    protected void onCreate(){
        this.createdTime = LocalDateTime.now();
    }
}
