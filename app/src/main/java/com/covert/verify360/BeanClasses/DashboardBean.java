package com.covert.verify360.BeanClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by user on 3/28/2018.
 */
public class DashboardBean implements Serializable {
    @SerializedName("status_name")
    @Expose
    @ColumnInfo(name = "status_name")
    @NonNull
    private String status_name;
    @SerializedName("No_of_Cases")
    @Expose
    @ColumnInfo(name = "No_of_Cases")
    @NonNull
    private int No_of_Cases;

    public DashboardBean(String status_name, int no_of_Cases) {
        this.status_name = status_name;
        this.No_of_Cases = no_of_Cases;
    }

    @NonNull
    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(@NonNull String status_name) {
        this.status_name = status_name;
    }

    @NonNull
    public int getNo_of_Cases() {
        return No_of_Cases;
    }

    public void setNo_of_Cases(@NonNull int no_of_Cases) {
        No_of_Cases = no_of_Cases;
    }
}
