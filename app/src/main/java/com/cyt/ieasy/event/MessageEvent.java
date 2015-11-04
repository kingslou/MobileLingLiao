package com.cyt.ieasy.event;

/**
 * Created by jin on 2015.10.26.
 */
public class MessageEvent {

    public final String Message;
    public  int step = 0 ;

    public MessageEvent(String message){
        this.Message = message;
    }

    public MessageEvent(String message,int step){
        this.Message = message;
        this.step = step;
    }

}
