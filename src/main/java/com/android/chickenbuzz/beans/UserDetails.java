package com.android.chickenbuzz.beans;

import java.io.Serializable;

/**
 * Created by 703145805 on 9/14/2016.
 */
public class UserDetails implements Serializable {

    private String user_id;
    private String first_name;
    private String last_name;
    private String height;
    private String email;
    private String gender;
    private String dob;
    private String image;
    private String isd_code;
    private String mobile_no;
    private String alternate_number;
    private String pin;
    private String emergency_pin;
    private String m_verify_code;
    private String refer_code;
    private String ref_user_id;
    private String is_active;
    private String date;

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIsd_code() {
        return isd_code;
    }

    public void setIsd_code(String isd_code) {
        this.isd_code = isd_code;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getAlternate_number() {
        return alternate_number;
    }

    public void setAlternate_number(String alternate_number) {
        this.alternate_number = alternate_number;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getEmergency_pin() {
        return emergency_pin;
    }

    public void setEmergency_pin(String emergency_pin) {
        this.emergency_pin = emergency_pin;
    }

    public String getM_verify_code() {
        return m_verify_code;
    }

    public void setM_verify_code(String m_verify_code) {
        this.m_verify_code = m_verify_code;
    }

    public String getRefer_code() {
        return refer_code;
    }

    public void setRefer_code(String refer_code) {
        this.refer_code = refer_code;
    }

    public String getRef_user_id() {
        return ref_user_id;
    }

    public void setRef_user_id(String ref_user_id) {
        this.ref_user_id = ref_user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
