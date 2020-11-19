package com.dangbinh.dinter.chat;

/**
 * Created by dangbinh on 18/11/2020.
 */

public class ChatObject {
    private String message;
    private Boolean currentUser;

    public ChatObject() {
    }

    public ChatObject(String message, Boolean currentUser){
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }

    public Boolean getCurrentUser(){
        return currentUser;
    }
    public void setCurrentUser(Boolean currentUser){
        this.currentUser = currentUser;
    }
}
