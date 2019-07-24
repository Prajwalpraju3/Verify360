package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormElementDatum {
    @SerializedName("main_section")
    @Expose
    private String mainSection;

    @SerializedName("is_multiple")
    @Expose
    private String is_multiple;

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

    public String getIs_multiple() {
        return is_multiple;
    }

    public void setIs_multiple(String is_multiple) {
        this.is_multiple = is_multiple;
    }

}
