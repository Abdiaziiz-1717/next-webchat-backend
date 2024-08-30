package com.ca217;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowedUsersId implements Serializable {
    @Id
    @Column(name = "UserEmail")
    private String userEmail;

    @Id
    @Column(name = "FollowedUserEmail")
    private String followedUserEmail;
}

