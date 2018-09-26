package com.covert.verify360.BeanClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("USER_LOGIN_ID")
    @Expose
    private String UserLoginID;
    @SerializedName("EMPLOYEE_NAME")
    @Expose
    private String EmployeeName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("ROLE_NAME")
    @Expose
    private String roleName;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("EMP_ID")
    @Expose
    private String EmpId;

    public User(String userLoginID, String employeeName, String password, String roleName, String userId, String empId) {
        UserLoginID = userLoginID;
        EmployeeName = employeeName;
        this.password = password;
        this.roleName = roleName;
        this.userId = userId;
        EmpId = empId;
    }

    public User() {
    }

    public String getUserLoginID() {
        return UserLoginID;
    }

    public void setUserLoginID(String userLoginID) {
        UserLoginID = userLoginID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmpId() {
        return EmpId;
    }

    public void setEmpId(String empId) {
        EmpId = empId;
    }


}
