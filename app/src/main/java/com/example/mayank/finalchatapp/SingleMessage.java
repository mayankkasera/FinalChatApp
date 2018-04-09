package com.example.mayank.finalchatapp;

/**
 * Created by mayank on 10/1/18.
 */

public class SingleMessage {
    String message;
    String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public SingleMessage(String message, String from) {
        this.message = message;
        this.from = from;
    }


    public SingleMessage()
    {

    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }
}
