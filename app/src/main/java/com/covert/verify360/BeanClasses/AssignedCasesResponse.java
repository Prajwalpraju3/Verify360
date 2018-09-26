package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignedCasesResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User")
    @Expose
    private List<NewCasesBean> newCasesList;

    public AssignedCasesResponse(String error, String message, List<NewCasesBean> newCasesList) {
        this.error = error;
        this.message = message;
        this.newCasesList = newCasesList;
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

    public List<NewCasesBean> getNewCasesList() {
        return newCasesList;
    }

    public void setNewCasesList(List<NewCasesBean> newCasesList) {
        this.newCasesList = newCasesList;
    }

}
