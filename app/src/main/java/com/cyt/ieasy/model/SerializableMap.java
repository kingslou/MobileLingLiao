package com.cyt.ieasy.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 自定义序列化map用于Activity间传递
 * Created by jin on 2015.11.19.
 */
public class SerializableMap implements Serializable {

    private HashMap<String,String[]> map;

    public HashMap<String, String[]> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String[]> map) {
        this.map = map;
    }
}
