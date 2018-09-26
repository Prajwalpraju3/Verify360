package com.covert.verify360.BeanClasses;

public class Items {
    private int checkedId;
    private String remarks;

    public Items(int checkedId, String remarks) {

        this.checkedId = checkedId;
        this.remarks = remarks;
    }

    public int getCheckedId() {
        return checkedId;
    }

    public void setCheckedId(int checkedId) {
        this.checkedId = checkedId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


}
