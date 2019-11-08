package com.stadio.task.model;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMessage {

    private String message;
    private Date sendTime;
    private String senderId;
    private String objectId;
    private String name;
    private String avatar;
    private String messageLabel;

    public ChatMessage() {
        this.sendTime = new Date();
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}
