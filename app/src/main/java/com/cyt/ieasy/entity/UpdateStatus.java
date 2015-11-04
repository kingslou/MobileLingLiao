package com.cyt.ieasy.entity;

/**
 * Created by jin on 2015.11.04.
 */
public class UpdateStatus {
    private Boolean success;
    private Boolean faile;
    private String  message;

    public void setFaile(Boolean faile) {
        this.faile = faile;
    }

    public Boolean getFaile() {
        return faile;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
