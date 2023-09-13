package com.for_cv.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ImageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    // @Lob means a large object in db like as byte[] or characters for images,videos,etc.
    // @Column(columnDefinition = "LONGBLOB") use to set type our db [MySQL]
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;
    // @JsonIgnore use it when we want to ignore this field in data response on client
    @JsonIgnore
    private Long userId;
    @JsonIgnore
    private Long postId;
}
