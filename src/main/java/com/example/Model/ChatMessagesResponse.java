package com.example.Model;

import java.util.List;

public class ChatMessagesResponse {
    List<ChatMessagesR> chatMessagesList;
    String currentUserEmail;
    Long currentUserId;

    public List<ChatMessagesR> getChatMessagesList() {
        return chatMessagesList;
    }

    public void setChatMessagesList(List<ChatMessagesR> chatMessagesList) {
        this.chatMessagesList = chatMessagesList;
    }

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }
}

