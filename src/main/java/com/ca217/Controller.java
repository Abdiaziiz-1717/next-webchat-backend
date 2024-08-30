package com.ca217;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    @Autowired
    private service service;

    @GetMapping("/getUsers")
    public ResponseEntity<Map<String, Object>> FindAllUsers() {
        Map<String, Object> response = service.findAllUsers();
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signUpUser(@RequestBody UsersTable user) {
        Map<String, Object> response = service.signUpUser(user.getName(), user.getEmail(), user.getPassword(), user.getUserPic());
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody UsersTable user) {
        Map<String, Object> response;
        String CheckUser = service.checkUser(user.getEmail(), user.getPassword());
        if (CheckUser.equals("User Exists")){
            response=service.findUserByEmail(user.getEmail());
           return ResponseEntity.status(200).body(response);
        }
        // Return a JSON-serializable response
        Map<String, Object> json = new HashMap<>();
        json.put("message", service.checkUser(user.getEmail(), user.getPassword()));
        response=json;
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/follow")
    public ResponseEntity<Map<String, Object>> followUser(@RequestBody FollowedUsersTable followedUser) {
        return ResponseEntity.status(200).body(service.followUser(followedUser));
    }

    @PostMapping("/unfollow")
    public ResponseEntity<Map<String, Object>> unfollowUser(@RequestBody FollowedUsersTable followedUser) {
        return ResponseEntity.status(200).body(service.unfollowUser(followedUser.getUserEmail(), followedUser.getFollowedUserEmail()));
    }

    @PostMapping("/followedUsers")
    public ResponseEntity<Map<String, Object>> getFollowedUsers(@RequestBody FollowedUsersTable followedUser) {
        return ResponseEntity.status(200).body(service.getFollowedUsers(followedUser.getUserEmail()));
    }

    @PostMapping("/followers")
    public ResponseEntity<Map<String, Object>> getFollowers(@RequestBody FollowedUsersTable follower) {
        return ResponseEntity.status(200).body(service.getFollowers(follower.getUserEmail()));
    }

    @PostMapping("/getMessages")
    public List<Message> getMessagesBetweenUsers(@RequestBody Message message) {
        return service.getMessagesBetweenUsers(message.getSenderEmail(), message.getReceiverEmail());
    }

    @PostMapping("/SaveMessage")
    public Message saveMessage(@RequestBody Message message) {
        return service.saveMessage(message);
    }

}
