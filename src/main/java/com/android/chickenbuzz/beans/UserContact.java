package com.android.chickenbuzz.beans;

import java.io.Serializable;

/**
 * Created by 703145805 on 9/11/2016.
 */
public class UserContact implements Serializable {

    private String name;
    private String phoneNo;
    private String phoneType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }
}
