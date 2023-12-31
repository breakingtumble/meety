package com.example.meety1.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    public User(String firstName, String lastName, String privateInfo, String openInfo, String email, String password, List<Interest> interests) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.privateInfo = privateInfo;
        this.openInfo = openInfo;
        this.email = email;
        this.password = password;
        this.interests = interests;
        this.countedRowsIndex = 0L;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "private_info")
    private String privateInfo;
    @Column(name="open_info")
    private String openInfo;
    private String email;
    private String password;
    @Column(name = "counted_rows")
    private Long countedRowsIndex;
    // Unidirectional association
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_interests",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id"))
    private List<Interest> interests;
}
