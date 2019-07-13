package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OuterSubSec {

    @SerializedName("sub_section")
    @Expose
    private List<InnerSubSection> innerSubSection = null;

    public List<InnerSubSection> getInnerSubSection() {
        return innerSubSection;
    }

    public void setInnerSubSection(List<InnerSubSection> innerSubSection) {
        this.innerSubSection = innerSubSection;
    }
}
