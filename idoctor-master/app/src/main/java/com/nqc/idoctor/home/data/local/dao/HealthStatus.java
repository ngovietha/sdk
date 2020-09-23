/*
 * Created by NQC on 5/26/20 5:44 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:44 AM
 *
 */

package com.nqc.idoctor.home.data.local.dao;

public class HealthStatus {
    private String nameStatus;
    private String value;
    private String time;
    private int imageRes;

    public HealthStatus(int imageRes, String nameStatus, String value, String time) {
        this.nameStatus = nameStatus;
        this.value = value;
        this.time = time;
        this.imageRes = imageRes;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
