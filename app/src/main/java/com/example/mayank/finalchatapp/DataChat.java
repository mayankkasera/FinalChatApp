package com.example.mayank.finalchatapp;

/**
 * Created by mayank on 11/1/18.
 */

public class DataChat {
    private String LastMessage;

    public DataChat() {
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public DataChat(String lastMessage) {
        LastMessage = lastMessage;
    }
}
