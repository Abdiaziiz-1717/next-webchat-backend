package com.ca217;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderEmailAndReceiverEmail(String senderEmail, String receiverEmail);
    List<Message> findByReceiverEmailAndSenderEmail(String receiverEmail, String senderEmail);
    void deleteBySenderEmailAndReceiverEmail(String senderEmail, String receiverEmail);
    void deleteByReceiverEmailAndSenderEmail(String senderEmail, String receiverEmail);
}
