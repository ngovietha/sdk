/*
 * Created by NQC on 4/25/20 8:10 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.remote.service;


import com.nqc.idoctor.common.data.remote.response.BaseResponse;

import io.reactivex.Single;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * BaseService
 */
public interface BaseService {

    /**
     * Ex: Get base data
     */
    //@TODO: Remove this
//    @Headers({"Content-Type: application/json"})
//    @POST("/api/get_base_data")
//    Single<BaseResponse> getBaseResponse(@Header("Authorization") String authHeader);
}
