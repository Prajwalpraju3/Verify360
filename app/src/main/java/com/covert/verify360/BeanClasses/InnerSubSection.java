package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InnerSubSection {
    @SerializedName("sub_section")
    @Expose
    private String subSection;

    private boolean mandatory,havedata;
    private boolean catagory_selected;

    public boolean isCatagory_selected() {
        return catagory_selected;
    }

    public void setCatagory_selected(boolean catagory_selected) {
        this.catagory_selected = catagory_selected;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isHavedata() {
        return havedata;
    }

    public void setHavedata(boolean havedata) {
        this.havedata = havedata;
    }

    @SerializedName("optionssection")
    @Expose
    private List<Optionssection> optionssection = null;

    private String builder = "";

    public String getSubSection() {
        return subSection;
    }

    public void setSubSection(String subSection) {
        this.subSection = subSection;
    }

    public List<Optionssection> getOptionssection() {
        return optionssection;
    }

    public void setOptionssection(List<Optionssection> optionssection) {
        this.optionssection = optionssection;
    }

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }
}
