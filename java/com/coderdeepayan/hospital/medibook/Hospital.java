package com.coderdeepayan.hospital.medibook;

public class Hospital {
    private String name, address,code;

    public static final int UTILITIES = 125;
    public static final int HOSPITAL = 256;
    private int viewType;

    public Hospital(int viewType) {
        this.viewType = viewType;
    }

    public Hospital(String name, String address, String code, int viewType) {
        this.name = name;
        this.address = address;
        this.code = code;
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCode() {
        return code;
    }
}
