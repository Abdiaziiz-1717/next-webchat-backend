package com.ca217;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "followed_users")
@IdClass(FollowedUsersId.class)

public class FollowedUsersTable implements Serializable {
    @Id
    @Column(name = "user_email")
    private String userEmail;

    @Id
    @Column(name = "followed_user_email")
    private String followedUserEmail;
}

