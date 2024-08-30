package com.ca217;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowedUsersRepository extends JpaRepository<FollowedUsersTable, FollowedUsersId> {

    // Custom query to find all followed users for a given user
    List<FollowedUsersTable> findByUserEmail(String userEmail);

    // Custom query to find all followers of a given user
    List<FollowedUsersTable> findByFollowedUserEmail(String followedUserEmail);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserEmailAndFollowedUserEmail(String userEmail, String followedUserEmail);
}
