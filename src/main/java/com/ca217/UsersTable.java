package com.ca217;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UsersTable {

    @Id
    @Column(name = "Email")
    private String email;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Password", nullable = false)
    private String password;

    @Column(name = "User_Pic", columnDefinition = "TEXT")
    private String userPic;
}
