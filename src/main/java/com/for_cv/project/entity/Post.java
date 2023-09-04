package com.for_cv.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
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
    private Set<String> likeUsers = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // Use Fetchtype.EAGER because if we want to download post we could be watch comments
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();
    @Column(updatable = false)
    private LocalDateTime createDate;
    @PrePersist
    protected void onCreate(){
        this.createDate = LocalDateTime.now();
    }
}
