/*
 * Created by NQC on 4/25/20 8:04 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.remote;

import com.google.gson.annotations.SerializedName;

public class Common {

    @SerializedName("latest_notice_timestamp")
    private String latestNoticeTimeStamp;

    public String getLatestNoticeTimeStamp() {
        return latestNoticeTimeStamp;
    }

    public void setLatestNoticeTimeStamp(String latestNoticeTimeStamp) {
        this.latestNoticeTimeStamp = latestNoticeTimeStamp;
    }
}
