package com.cyt.ieasy.event;

/**
 * 使用EventBus代替Activity间的Intent传递
 * Created by jin on 2015.11.13.
 */
public class LL_Event {

    public String LL_CODE;

    public int LL_STATUS;

    public LL_Event(String LL_CODE, int LL_STATUS){
        this.LL_CODE = LL_CODE;
        this.LL_STATUS = LL_STATUS;
    }
}
