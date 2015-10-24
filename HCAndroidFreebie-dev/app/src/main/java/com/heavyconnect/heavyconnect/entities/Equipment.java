package com.heavyconnect.heavyconnect.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by felipepx on 9/2/15.
 */
public class Equipment implements Serializable {

    public static final int STATUS_OK = 1;
    public static final int STATUS_SERVICE = 2;
    public static final int STATUS_BROKEN = 3;

    private int id;
    private int user;
    private String name;

    @SerializedName("model_number")
    private String modelNumber;

    @SerializedName("asset_number")
    private int assetNumber;

    private int status;
    private int hours;

    private double longitude;
    private double latitude;

    private boolean changed = false;

    private String last_modification;

    private String bluetooth_address;

    public Equipment(){}

    public Equipment(String name, int status){
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public int getAssetNumber() {
        return assetNumber;
    }

    public void setAssetNumber(int assetNumber) {
        this.assetNumber = assetNumber;
    }

    public int getEngineHours() {
        return hours;
    }

    public void setEngineHours(int hours) {
        this.hours = hours;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLast_modified() { return last_modification; }

    public void setLast_modified(String last_modified) { this.last_modification = last_modified; }

    public boolean getWasChanged(){
        return changed;
    }

    public void setWasChanged(boolean changed){
        this.changed = changed;
    }

    public String getBluetoothAddress(){
        return bluetooth_address;
    }

    public void setBluetoothAddress(String bluetooth_address){
        this.bluetooth_address = bluetooth_address;
    }

}
