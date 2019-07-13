package com.covert.verify360.BeanClasses;

import android.arch.persistence.room.ColumnInfo;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaseDetail {
    @SerializedName("CASE_ID")
    @Expose
    private Integer cASEID;
    @SerializedName("case_detailed_id")
    @Expose
    private Integer caseDetailedId;
    @SerializedName("PRE_POST")
    @Expose
    private String pREPOST;
    @SerializedName("Product_type")
    @Expose
    private String productType;
    @SerializedName("Client_Name")
    @Expose
    private String clientName;
    @SerializedName("APPLICANT_FIRST_NAME")
    @Expose
    private String aPPLICANTFIRSTNAME;
    @SerializedName("APPLICANT_LAST_NAME")
    @Expose
    private String aPPLICANTLASTNAME;
    @SerializedName("FATHER_NAME")
    @Expose
    private String fATHERNAME;
    @SerializedName("PRIMARY_CONTACT_NO")
    @Expose
    private String pRIMARYCONTACTNO;
    @SerializedName("EMAIL_ID")
    @Expose
    private String eMAILID;
    @SerializedName("Activity_type")
    @Expose
    private String activityType;
    @SerializedName("COMPANY_NAME")
    @Expose
    private String cOMPANYNAME;
    @SerializedName("DOOR_NO")
    @Expose
    private String dOORNO;
    @SerializedName("STREET_ADDRESS")
    @Expose
    private String sTREETADDRESS;
    @SerializedName("CITY")
    @Expose
    private String cITY;
    @SerializedName("LADMARK")
    @Expose
    private String lADMARK;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("PINCODE")
    @Expose
    private String pINCODE;
    @SerializedName("Region_Name")
    @Expose
    private String regionName;
    @SerializedName("ESTIMATED_COMPLETED_DATE")
    @Expose
    private String eSTIMATEDCOMPLETEDDATE;
    @SerializedName("Remarks")
    @Expose
    private String remarks;
    @SerializedName("Assigned_to")
    @Expose
    private String assignedTo;
    @SerializedName("FILE_UPLOADED")
    @Expose
    private String fILEUPLOADED;
    @SerializedName("working_by")
    @Expose
    private Integer workingBy;

    public Integer getCASEID() {
        return cASEID;
    }

    public void setCASEID(Integer cASEID) {
        this.cASEID = cASEID;
    }

    public Integer getCaseDetailedId() {
        return caseDetailedId;
    }

    public void setCaseDetailedId(Integer caseDetailedId) {
        this.caseDetailedId = caseDetailedId;
    }

    public String getPREPOST() {
        return pREPOST;
    }

    public void setPREPOST(String pREPOST) {
        this.pREPOST = pREPOST;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAPPLICANTFIRSTNAME() {
        return aPPLICANTFIRSTNAME;
    }

    public void setAPPLICANTFIRSTNAME(String aPPLICANTFIRSTNAME) {
        this.aPPLICANTFIRSTNAME = aPPLICANTFIRSTNAME;
    }

    public String getAPPLICANTLASTNAME() {
        return aPPLICANTLASTNAME;
    }

    public void setAPPLICANTLASTNAME(String aPPLICANTLASTNAME) {
        this.aPPLICANTLASTNAME = aPPLICANTLASTNAME;
    }

    public String getFATHERNAME() {
        return fATHERNAME;
    }

    public void setFATHERNAME(String fATHERNAME) {
        this.fATHERNAME = fATHERNAME;
    }

    public String getPRIMARYCONTACTNO() {
        return pRIMARYCONTACTNO;
    }

    public void setPRIMARYCONTACTNO(String pRIMARYCONTACTNO) {
        this.pRIMARYCONTACTNO = pRIMARYCONTACTNO;
    }

    public String getEMAILID() {
        return eMAILID;
    }

    public void setEMAILID(String eMAILID) {
        this.eMAILID = eMAILID;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getCOMPANYNAME() {
        return cOMPANYNAME;
    }

    public void setCOMPANYNAME(String cOMPANYNAME) {
        this.cOMPANYNAME = cOMPANYNAME;
    }

    public String getDOORNO() {
        return dOORNO;
    }

    public void setDOORNO(String dOORNO) {
        this.dOORNO = dOORNO;
    }

    public String getSTREETADDRESS() {
        return sTREETADDRESS;
    }

    public void setSTREETADDRESS(String sTREETADDRESS) {
        this.sTREETADDRESS = sTREETADDRESS;
    }

    public String getCITY() {
        return cITY;
    }

    public void setCITY(String cITY) {
        this.cITY = cITY;
    }

    public String getLADMARK() {
        return lADMARK;
    }

    public void setLADMARK(String lADMARK) {
        this.lADMARK = lADMARK;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPINCODE() {
        return pINCODE;
    }

    public void setPINCODE(String pINCODE) {
        this.pINCODE = pINCODE;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getESTIMATEDCOMPLETEDDATE() {
        return eSTIMATEDCOMPLETEDDATE;
    }

    public void setESTIMATEDCOMPLETEDDATE(String eSTIMATEDCOMPLETEDDATE) {
        this.eSTIMATEDCOMPLETEDDATE = eSTIMATEDCOMPLETEDDATE;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getFILEUPLOADED() {
        return fILEUPLOADED;
    }

    public void setFILEUPLOADED(String fILEUPLOADED) {
        this.fILEUPLOADED = fILEUPLOADED;
    }

    public Integer getWorkingBy() {
        return workingBy;
    }

    public void setWorkingBy(Integer workingBy) {
        this.workingBy = workingBy;
    }
}

