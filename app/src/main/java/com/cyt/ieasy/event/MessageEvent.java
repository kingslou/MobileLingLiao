package com.cyt.ieasy.event;

/**
 * Created by jin on 2015.10.26.
 */
public class MessageEvent {

    public final String message;
    public  String error="";
    public  int step = 0 ;

    public MessageEvent(String message){
        this.message = message;
    }

    public MessageEvent(String message,String error,int step){
        this.message = message;
        this.step = step;
        this.error = error;
    }

}
