package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompletedCasesResponse {

    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User")
    @Expose
    private List<CompCasesUser> user;

    public CompletedCasesResponse(String error, String message, List<CompCasesUser> user) {
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

    public List<CompCasesUser> getUser() {
        return user;
    }

    public void setUser(List<CompCasesUser> user) {
        this.user = user;
    }
}
