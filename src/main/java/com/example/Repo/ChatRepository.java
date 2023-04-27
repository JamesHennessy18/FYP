package com.example.Repo;

import com.example.Model.ChatMessages;
import com.example.Model.User;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends CrudRepository<ChatMessages, Long> {

    public List<ChatMessages> findChatMessagesBySender(User user);
    public List<ChatMessages> findChatMessagesByReceiever(User user);
    public List<ChatMessages> findChatMessagesBySenderAndReceiever(User sender, User reciever);
    List<ChatMessages> findBySenderId(Long senderId);
    void deleteBySenderId(Long senderId);


}

