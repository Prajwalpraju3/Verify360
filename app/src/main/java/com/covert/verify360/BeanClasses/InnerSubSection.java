package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InnerSubSection {
    @SerializedName("sub_section")
    @Expose
    private String subSection;

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
