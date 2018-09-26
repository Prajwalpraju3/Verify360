package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormElementDatum {
    @SerializedName("main_section")
    @Expose
    private String mainSection;
    @SerializedName("sub_section")
    @Expose
    private List<InnerSubSection> outerSubSection = null;


    public String getMainSection() {
        return mainSection;
    }

    public void setMainSection(String mainSection) {
        this.mainSection = mainSection;
    }

    public List<InnerSubSection> getOuterSubSection() {
        return outerSubSection;
    }

    public void setOuterSubSection(List<InnerSubSection> outerSubSection) {
        this.outerSubSection = outerSubSection;
    }

}
