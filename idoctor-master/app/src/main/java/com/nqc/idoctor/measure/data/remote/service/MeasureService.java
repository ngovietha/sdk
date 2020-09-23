/*
 * Created by NQC on 5/23/20 7:41 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/23/20 7:41 PM
 *
 */

package com.nqc.idoctor.measure.data.remote.service;

import com.nqc.idoctor.common.data.remote.response.BaseResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * all of functions for account user
 */
public interface MeasureService {

    @Headers({"Content-Type: application/json"})
    @POST("/ecg.php")
    Single<BaseResponse> postECGData(@Field("ecgData") String ecgData);
}
