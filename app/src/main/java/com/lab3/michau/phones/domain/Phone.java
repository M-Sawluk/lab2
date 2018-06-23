package com.lab3.michau.phones.domain;

import java.io.Serializable;

public class Phone implements Serializable {
    private long ID;
    private String model;
    private String manufacturer;
    private String androidVer;
    private String www;

    public Phone(long ID, String model, String manufacturer, String androidVer, String www) {
        this.ID = ID;
        this.model = model;
        this.manufacturer = manufacturer;
        this.androidVer = androidVer;
        this.www = www;
    }

    public long getID() { return ID; }

    public String getModel() {return model; }

    public String getManufacturer() { return manufacturer; }

    public String getAndroidVer() { return androidVer; }

    public String getWww() { return www; }
}
