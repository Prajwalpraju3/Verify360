package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CancelledReasonsList implements Serializable{

    @SerializedName("cancelled_reason")
    @Expose
    private String cancelledReasonText;
    @SerializedName("Cancelled_ID")
    @Expose
    private String case_detail_id;
    @SerializedName("trans")
    @Expose
    private String transString;


    public CancelledReasonsList(String cancelledReasonText, String case_detail_id, String transString) {
        this.cancelledReasonText = cancelledReasonText;
        this.case_detail_id = case_detail_id;
        this.transString = transString;
    }


    public String getCancelledReasonText() {
        return cancelledReasonText;
    }

    public void setCancelledReasonText(String cancelledReasonText) {
        this.cancelledReasonText = cancelledReasonText;
    }

    public String getCase_detail_id() {
        return case_detail_id;
    }

    public void setCase_detail_id(String case_detail_id) {
        this.case_detail_id = case_detail_id;
    }

    public String getTransString() {
        return transString;
    }

    public void setTransString(String transString) {
        this.transString = transString;
    }
}
