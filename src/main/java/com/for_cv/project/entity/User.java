package com.for_cv.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.for_cv.project.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String lastname;
    @Column(unique = true)
    private String email;
    @Column(columnDefinition = "text")
    private String bio;
    @Column(length = 3000)
    private String password;

    @ElementCollection(targetClass = ERole.class)
    @CollectionTable(name = "user_role",
            joinColumns = @JoinColumn(referencedColumnName = "userId")
    )
    private Set<ERole> roles = new HashSet<>();

    //CascadeType.ALL this means when we will want to delete User than all posts will delete also
     /* FetchType.LAZY we should use this fetch type because if we don't want to appeal for sub-entity in our
       main-entity for overloading our select query from db */
    /* orphanRemoval = true --> use when we want to delete posts from db */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(updatable = false)
    private LocalDateTime createDate;

    private Collection<? extends GrantedAuthority>

    //@PrePersist annotation get used to save our field as 'date' before save User as and Object in db
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }


}
