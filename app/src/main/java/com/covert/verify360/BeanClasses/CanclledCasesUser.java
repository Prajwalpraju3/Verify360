package com.covert.verify360.BeanClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "cancelled_cases", primaryKeys = {"CASE_ID", "case_detail_id"})
public class CanclledCasesUser implements Serializable {

    @SerializedName("CASE_ID")
    @Expose
    @ColumnInfo(name = "CASE_ID")
    @NonNull
    private String case_id;
    @SerializedName("case_detailed_id")
    @Expose
    @ColumnInfo(name = "case_detail_id")
    @NonNull
    private String case_detail_id;
    @SerializedName("PRE_POST")
    @Expose
    @ColumnInfo(name = "PRE_POST")
    private String pre_post;
    @SerializedName("Product_type")
    @Expose
    @ColumnInfo(name = "Product_type")
    private String product_type;
    @SerializedName("Client_Name")
    @Expose
    @ColumnInfo(name = "Client_Name")
    private String client_name;
    @SerializedName("APPLICANT_FIRST_NAME")
    @Expose
    @ColumnInfo(name = "APPLICANT_FIRST_NAME")
    private String applicant_first_name;
    @SerializedName("APPLICANT_LAST_NAME")
    @Expose
    @ColumnInfo(name = "APPLICANT_LAST_NAME")
    private String applicant_last_name;
    @SerializedName("FATHER_NAME")
    @Expose
    @ColumnInfo(name = "FATHER_NAME")
    private String father_name;
    @SerializedName("PRIMARY_CONTACT_NO")
    @Expose
    @ColumnInfo(name = "PRIMARY_CONTACT_NO")
    private String primary_contact;
    @SerializedName("EMAIL_ID")
    @Expose
    @ColumnInfo(name = "EMAIL_ID")
    private String email;
    @SerializedName("Activity_type")
    @Expose
    @ColumnInfo(name = "Activity_type")
    private String activity_type;
    @SerializedName("COMPANY_NAME")
    @Expose
    @ColumnInfo(name = "COMPANY_NAME")
    private String company_name;
    @SerializedName("DOOR_NO")
    @Expose
    @ColumnInfo(name = "DOOR_NO")
    private String door_number;
    @SerializedName("STREET_ADDRESS")
    @Expose
    @ColumnInfo(name = "STREET_ADDRESS")
    private String street_address;
    @SerializedName("CITY")
    @Expose
    @ColumnInfo(name = "CITY")
    private String city;
    @SerializedName("LADMARK")
    @Expose
    @ColumnInfo(name = "LADMARK")
    private String landmark;
    @SerializedName("Location")
    @Expose
    @ColumnInfo(name = "Location")
    private String location;
    @SerializedName("PINCODE")
    @Expose
    @ColumnInfo(name = "PINCODE")
    private String pincode;
    @SerializedName("Region_Name")
    @Expose
    @ColumnInfo(name = "Region_Name")
    private String region_name;
    @SerializedName("Cancelled_on")
    @Expose
    @ColumnInfo(name = "Cancelled_on")
    private String cancelledOn;

    @Expose
    @SerializedName("Remarks")
    @ColumnInfo(name = "Remarks")
    private String remarks;
    @SerializedName("working_by")
    @Expose
    @ColumnInfo(name = "working_by")
    private String working_by;
    @SerializedName("Cancelled_reason")
    @Expose
    @ColumnInfo(name = "Cancelled_reason")
    private String Cancelled_reason;
    @SerializedName("Cancelled_reason_description")
    @Expose
    @ColumnInfo(name = "Cancelled_reason_description")
    private String Cancelled_reason_description;


    public CanclledCasesUser(@NonNull String case_id, @NonNull String case_detail_id, String pre_post, String product_type, String client_name, String applicant_first_name, String applicant_last_name, String father_name, String primary_contact, String email, String activity_type, String company_name, String door_number, String street_address, String city, String landmark, String location, String pincode, String region_name, String cancelledOn, String remarks, String working_by, String cancelled_reason, String cancelled_reason_description) {
        this.case_id = case_id;
        this.case_detail_id = case_detail_id;
        this.pre_post = pre_post;
        this.product_type = product_type;
        this.client_name = client_name;
        this.applicant_first_name = applicant_first_name;
        this.applicant_last_name = applicant_last_name;
        this.father_name = father_name;
        this.primary_contact = primary_contact;
        this.email = email;
        this.activity_type = activity_type;
        this.company_name = company_name;
        this.door_number = door_number;
        this.street_address = street_address;
        this.city = city;
        this.landmark = landmark;
        this.location = location;
        this.pincode = pincode;
        this.region_name = region_name;
        this.cancelledOn = cancelledOn;
        this.remarks = remarks;
        this.working_by = working_by;
        Cancelled_reason = cancelled_reason;
        Cancelled_reason_description = cancelled_reason_description;
    }

    @NonNull
    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(@NonNull String case_id) {
        this.case_id = case_id;
    }

    @NonNull
    public String getCase_detail_id() {
        return case_detail_id;
    }

    public void setCase_detail_id(@NonNull String case_detail_id) {
        this.case_detail_id = case_detail_id;
    }

    public String getPre_post() {
        return pre_post;
    }

    public void setPre_post(String pre_post) {
        this.pre_post = pre_post;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getApplicant_first_name() {
        return applicant_first_name;
    }

    public void setApplicant_first_name(String applicant_first_name) {
        this.applicant_first_name = applicant_first_name;
    }

    public String getApplicant_last_name() {
        return applicant_last_name;
    }

    public void setApplicant_last_name(String applicant_last_name) {
        this.applicant_last_name = applicant_last_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getPrimary_contact() {
        return primary_contact;
    }

    public void setPrimary_contact(String primary_contact) {
        this.primary_contact = primary_contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDoor_number() {
        return door_number;
    }

    public void setDoor_number(String door_number) {
        this.door_number = door_number;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getRegion_name() {
        return region_name;
    }

    public void setRegion_name(String region_name) {
        this.region_name = region_name;
    }

    public String getCancelledOn() {
        return cancelledOn;
    }

    public void setCancelledOn(String cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getWorking_by() {
        return working_by;
    }

    public void setWorking_by(String working_by) {
        this.working_by = working_by;
    }

    public String getCancelled_reason() {
        return Cancelled_reason;
    }

    public void setCancelled_reason(String cancelled_reason) {
        Cancelled_reason = cancelled_reason;
    }

    public String getCancelled_reason_description() {
        return Cancelled_reason_description;
    }

    public void setCancelled_reason_description(String cancelled_reason_description) {
        Cancelled_reason_description = cancelled_reason_description;
    }
}
