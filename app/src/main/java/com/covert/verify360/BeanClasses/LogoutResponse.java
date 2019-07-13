package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogoutResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User")
    @Expose
    private List<LogoutUser> user;

    public LogoutResponse(String error, String message, User[] user) {
        this.error = error;
        this.message = message;
        user=user;
    }

    public LogoutResponse() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LogoutUser> getUser() {
        return user;
    }

    public void setUser(List<LogoutUser> user) {
        this.user = user;
    }

}
