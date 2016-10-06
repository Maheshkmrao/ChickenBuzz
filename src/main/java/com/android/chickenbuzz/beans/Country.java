package com.android.chickenbuzz.beans;

/**
 * Created by 703145805 on 9/15/2016.
 */
public class Country {
    private String name;
    private String iso;
    private int dialCode;

    public Country(String name, String iso, int dialCode) {
        this.setName(name);
        this.setIso(iso);
        this.setDialCode(dialCode);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return this.iso;
    }

    public void setIso(String iso) {
        this.iso = iso.toUpperCase();
    }

    public int getDialCode() {
        return this.dialCode;
    }

    public void setDialCode(int dialCode) {
        this.dialCode = dialCode;
    }

    public boolean equals(Object o) {
        return o instanceof Country && ((Country)o).getIso().toUpperCase().equals(this.getIso().toUpperCase());
    }
}
