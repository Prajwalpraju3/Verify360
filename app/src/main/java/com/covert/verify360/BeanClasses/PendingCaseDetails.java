package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PendingCaseDetails {
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("Case_details")
    @Expose
    private List<CaseDetail> caseDetails = null;

    @SerializedName("form_element_data")
    @Expose
    private List<FormElementDatum> formElementData = null;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CaseDetail> getCaseDetails() {
        return caseDetails;
    }

    public void setCaseDetails(List<CaseDetail> caseDetails) {
        this.caseDetails = caseDetails;
    }

    public List<FormElementDatum> getFormElementData() {
        return formElementData;
    }

    public void setFormElementData(List<FormElementDatum> formElementData) {
        this.formElementData = formElementData;
    }
}
