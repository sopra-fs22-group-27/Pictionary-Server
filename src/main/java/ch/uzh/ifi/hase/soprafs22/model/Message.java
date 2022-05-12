package ch.uzh.ifi.hase.soprafs22.model;

import ch.uzh.ifi.hase.soprafs22.constant.ChatStatus;

public class Message {
    private String senderUsername;
    private String receiverUsername;
    private String message;
    private ChatStatus chatStatus;

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setChatStatus(ChatStatus chatStatus) {
        this.chatStatus = chatStatus;
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }

}