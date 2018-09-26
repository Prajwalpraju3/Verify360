package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseMessage {
    @SerializedName("error")
    @Expose
    private Boolean error;

    public ResponseMessage(boolean error) {
        this.error = error;
    }

    public Boolean isError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
