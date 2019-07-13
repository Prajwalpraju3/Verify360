package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Optionssection {
    @SerializedName("form_element_id")
    @Expose
    private Integer formElementId;
    @SerializedName("options")
    @Expose
    private String options;

    private boolean isSelected = false;

    public Integer getFormElementId() {
        return formElementId;
    }

    public void setFormElementId(Integer formElementId) {
        this.formElementId = formElementId;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
