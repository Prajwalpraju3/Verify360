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

    @SerializedName("mandatory_option")
    @Expose
    private String mandatory_option;

    public String getMandatory_option() {
        return mandatory_option;
    }

    public void setMandatory_option(String mandatory_option) {
        this.mandatory_option = mandatory_option;
    }

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
