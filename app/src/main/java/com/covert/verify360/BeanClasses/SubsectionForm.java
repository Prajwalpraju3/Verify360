package com.covert.verify360.BeanClasses;

public class SubsectionForm {
    private int form_element_id;
    private String remarks;

    public SubsectionForm(int form_element_id, String remarks) {
        this.form_element_id = form_element_id;
        this.remarks = remarks;
    }

    public int getForm_element_id() {
        return form_element_id;
    }

    public void setForm_element_id(int form_element_id) {
        this.form_element_id = form_element_id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
