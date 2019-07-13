package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingCasesResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User")
    @Expose
    private List<PendingCasesBean> pendingCasesBeanList;


    public PendingCasesResponse(String error, String message, List<PendingCasesBean> pendingCasesBeanList) {
        this.error = error;
        this.message = message;
        this.pendingCasesBeanList = pendingCasesBeanList;
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

    public List<PendingCasesBean> getNewCasesList() {
        return pendingCasesBeanList;
    }

    public void setNewCasesList(List<PendingCasesBean> pendingCasesBeanList) {
        this.pendingCasesBeanList = pendingCasesBeanList;
    }
}
