package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dashboard {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User")
    @Expose
    private List<DashboardBean> dashboardData;

    public Dashboard(String error, String message, List<DashboardBean> dashboardData) {
        this.error = error;
        this.message = message;
        this.dashboardData = dashboardData;
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

    public List<DashboardBean> getNewCasesList() {
        return dashboardData;
    }

    public void setNewCasesList(List<DashboardBean> newCasesList) {
        this.dashboardData = newCasesList;
    }
}
