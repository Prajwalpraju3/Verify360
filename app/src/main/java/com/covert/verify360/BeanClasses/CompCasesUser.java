package com.covert.verify360.BeanClasses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "completed_cases", primaryKeys = {"CASE_ID", "case_detail_id"})
public class CompCasesUser implements Serializable{

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
    @SerializedName("Completed_Date")
    @Expose
    @ColumnInfo(name = "Completed_Date")
    private String completed_date;
    @SerializedName("ESTIMATED_COMPLETED_DATE")
    @Expose
    @ColumnInfo(name = "ESTIMATED_COMPLETED_DATE")
    private String est_completed_date;
    @Expose
    @SerializedName("Remarks")
    @ColumnInfo(name = "Remarks")
    private String remarks;
    @SerializedName("FILE_UPLOADED")
    @Expose
    @ColumnInfo(name = "FILE_UPLOADED")
    private String file_url;
    @SerializedName("Completed_in_TAT")
    @Expose
    @ColumnInfo(name = "Completed_in_TAT")
    private String completed_in_tat;
    @SerializedName("Priority")
    @Expose
    @ColumnInfo(name = "Priority")
    private String priority;
    @SerializedName("working_by")
    @Expose
    @ColumnInfo(name = "working_by")
    private String working_by;


    public CompCasesUser(String case_id, String case_detail_id, String pre_post, String product_type, String client_name, String applicant_first_name, String applicant_last_name, String father_name, String primary_contact, String email, String activity_type, String company_name, String door_number, String street_address, String city, String landmark, String location, String pincode, String region_name, String completed_date, String est_completed_date, String remarks, String file_url, String completed_in_tat, String priority, String working_by) {
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
        this.completed_date = completed_date;
        this.est_completed_date = est_completed_date;
        this.remarks = remarks;
        this.file_url = file_url;
        this.completed_in_tat = completed_in_tat;
        this.priority = priority;
        this.working_by = working_by;
    }

    public String getCase_id() {
        return case_id;
    }

    public void setCase_id(String case_id) {
        this.case_id = case_id;
    }

    public String getCase_detail_id() {
        return case_detail_id;
    }

    public void setCase_detail_id(String case_detail_id) {
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

    public String getCompleted_date() {
        return completed_date;
    }

    public void setCompleted_date(String completed_date) {
        this.completed_date = completed_date;
    }

    public String getEst_completed_date() {
        return est_completed_date;
    }

    public void setEst_completed_date(String est_completed_date) {
        this.est_completed_date = est_completed_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getCompleted_in_tat() {
        return completed_in_tat;
    }

    public void setCompleted_in_tat(String completed_in_tat) {
        this.completed_in_tat = completed_in_tat;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWorking_by() {
        return working_by;
    }

    public void setWorking_by(String working_by) {
        this.working_by = working_by;
    }
}
