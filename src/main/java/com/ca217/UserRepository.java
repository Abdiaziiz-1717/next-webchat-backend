package com.ca217;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UsersTable, String> {

    @Query("SELECT u FROM UsersTable u WHERE u.email = :email AND u.password = :password")
    UsersTable findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'User Exists' ELSE 'User Does Not Exist' END FROM UsersTable u WHERE u.email = :email AND u.password = :password")
    String checkUser(@Param("email") String email, @Param("password") String password);
}
