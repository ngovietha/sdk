/*
 * Created by NQC on 5/28/20 6:21 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:56 AM
 *
 */

package com.nqc.idoctor.home.data.local.dao;

public class MeasureType {
    private String nameStatus;
    private int imageRes;
    private int backgroundRes;

    public MeasureType(String nameStatus, int imageRes, int backgroundRes) {
        this.nameStatus = nameStatus;
        this.imageRes = imageRes;
        this.backgroundRes = backgroundRes;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getBackgroundRes() {
        return backgroundRes;
    }

    public void setBackgroundRes(int backgroundRes) {
        this.backgroundRes = backgroundRes;
    }
}
