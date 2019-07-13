package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CancelledCasesResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User")
    @Expose
    private List<CanclledCasesUser> user;

    public CancelledCasesResponse(String error, String message, List<CanclledCasesUser> user) {
        this.error = error;
        this.message = message;
        this.user = user;
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

    public List<CanclledCasesUser> getUser() {
        return user;
    }

    public void setUser(List<CanclledCasesUser> user) {
        this.user = user;
    }
}
