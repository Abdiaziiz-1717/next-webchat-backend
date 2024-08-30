package com.ca217;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class service {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowedUsersRepository followedUsersRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Transactional
    public Map<String, Object> signUpUser(String name, String email, String password, String userPic) {
        Map<String, Object> response = new HashMap<>();
        UsersTable newUser = new UsersTable();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setUserPic(userPic);
        userRepository.save(newUser);
        response.put("message", "User signed up successfully.");
        return response;
    }

    @Transactional
    public String checkUser(String email, String password) {
        UsersTable user = userRepository.findByEmailAndPassword(email, password);
        return (user != null) ? "User Exists" : "User Does Not Exist";
    }

    public Map<String, Object> findAllUsers() {
        Map<String, Object> response = new HashMap<>();
        response.put("result", userRepository.findAll());
        return response;
    }

    public Map<String, Object> findUserByEmail(String email) {
        Map<String, Object> response = new HashMap<>();
        Optional<UsersTable> userOpt = userRepository.findById(email);
        userOpt.ifPresent(user -> response.put("result", user));
        return response;
    }

    @Transactional
    public Map<String, Object> followUser(FollowedUsersTable followedUser) {
        Map<String, Object> response = new HashMap<>();
        FollowedUsersId id = new FollowedUsersId(followedUser.getUserEmail(), followedUser.getFollowedUserEmail());
        if (followedUsersRepository.existsById(id)) {
            response.put("message", "Already following " + followedUser.getFollowedUserEmail());
        } else {
            followedUsersRepository.save(followedUser);
            response.put("message", "Successfully followed " + followedUser.getFollowedUserEmail());
        }
        return response;
    }

    @Transactional
    public Map<String, Object> unfollowUser(String userEmail, String followedUserEmail) {
        Map<String, Object> response = new HashMap<>();
        FollowedUsersId id = new FollowedUsersId(userEmail, followedUserEmail);
        if (followedUsersRepository.existsById(id)) {
            followedUsersRepository.deleteById(id);
            response.put("message", "Successfully unfollowed " + followedUserEmail);
            boolean userFollowsOther = followedUsersRepository.existsByUserEmailAndFollowedUserEmail(userEmail, followedUserEmail);
            boolean otherFollowsUser = followedUsersRepository.existsByUserEmailAndFollowedUserEmail(followedUserEmail, userEmail);
            if (!userFollowsOther && !otherFollowsUser) {
                List<Message> messagesToDelete = messageRepository.findBySenderEmailAndReceiverEmail(userEmail, followedUserEmail);
                messagesToDelete.addAll(messageRepository.findByReceiverEmailAndSenderEmail(userEmail, followedUserEmail));
                messageRepository.deleteAll(messagesToDelete);
                response.put("message", response.get("message") + " Messages between " + userEmail + " and " + followedUserEmail + " have been deleted.");
            }
        } else {
            response.put("message", "Not following " + followedUserEmail);
        }
        return response;
    }

    public Map<String, Object> getFollowedUsers(String email) {
        Map<String, Object> response = new HashMap<>();
        List<FollowedUsersTable> followedUsers = followedUsersRepository.findByUserEmail(email);
        Map<Integer, UsersTable> followedUserDetails = new HashMap<>();
        for (FollowedUsersTable followedUser : followedUsers) {
            Optional<UsersTable> userDetailsOpt = userRepository.findById(followedUser.getFollowedUserEmail());
            userDetailsOpt.ifPresent(user -> followedUserDetails.put(followedUserDetails.size(), user));
        }
        response.put("result", followedUserDetails);
        return response;
    }

    @Transactional
    public Map<String, Object> getFollowers(String email) {
        Map<String, Object> response = new HashMap<>();
        List<FollowedUsersTable> followers = followedUsersRepository.findByFollowedUserEmail(email);
        Map<Integer, UsersTable> followersDetails = new HashMap<>();
        for (FollowedUsersTable follower : followers) {
            Optional<UsersTable> userDetailsOpt = userRepository.findById(follower.getUserEmail());
            userDetailsOpt.ifPresent(user -> followersDetails.put(followersDetails.size(), user));
        }
        response.put("result", followersDetails);
        return response;
    }

    public List<Message> getMessagesBetweenUsers(String SenderEmail, String ReceiverEmail) {
        List<Message> sentMessages = messageRepository.findBySenderEmailAndReceiverEmail(SenderEmail, ReceiverEmail);
        List<Message> receivedMessages = messageRepository.findByReceiverEmailAndSenderEmail(SenderEmail, ReceiverEmail);
        sentMessages.addAll(receivedMessages);
        return sentMessages.stream()
                .sorted((m1, m2) -> m1.getTimestamp().compareTo(m2.getTimestamp()))
                .collect(Collectors.toList());
    }

    public Message saveMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
