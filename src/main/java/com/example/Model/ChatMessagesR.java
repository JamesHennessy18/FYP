package com.example.Model;

import java.time.LocalDateTime;

public class ChatMessagesR {

    private long chatId;

    private String content;

    private Sender sender;

    private Receiver receiever;

    private LocalDateTime createdAt;

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiever() {
        return receiever;
    }

    public void setReceiever(Receiver receiever) {
        this.receiever = receiever;
    }
}
