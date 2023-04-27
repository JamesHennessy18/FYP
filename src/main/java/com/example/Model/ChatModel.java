package com.example.Model;

import javax.persistence.*;

public class ChatModel {
    private Integer receiever;
    private String content;
    private Integer sender;

    public Integer getReceiever() {
        return receiever;
    }

    public void setReceiever(Integer receiever) {
        this.receiever = receiever;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }
}
