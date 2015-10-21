package com.cyt.ieasy.event;

/**
 * Created by jin on 2015.10.20.
 */
public class LoginMessage {
    private String message;
    private int status;
    public LoginMessage(String message){
        this.message =  message;
    }
    public boolean loginSuccess(){
        return true;
    }
    public boolean loginFaile(){
        return false;
    }
}
