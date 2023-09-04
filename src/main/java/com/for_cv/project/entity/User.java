package com.for_cv.project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.for_cv.project.entity.enums.ERole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class User implements UserDetails {

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
            joinColumns = @JoinColumn(name = "user_id")
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

    // @Transient means field as authorities won't be stored in the database
    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    //@PrePersist annotation get used to save our field as 'date' before save User as and Object in db
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    /**
     * SECURITY
     */

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
