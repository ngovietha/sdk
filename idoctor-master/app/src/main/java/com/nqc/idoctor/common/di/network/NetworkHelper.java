/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.network;

import android.content.Context;

import com.google.gson.Gson;
import com.nqc.idoctor.R;
import com.nqc.idoctor.common.data.remote.response.BaseResponse;
import com.nqc.idoctor.common.utils.network.AppException;

import okhttp3.Response;

/**
 * NetworkHelper
 */
public class NetworkHelper {

    public static void handleNetworkError(Context context, Response response, Gson gson) {

        if (response.body() != null) {
            BaseResponse errorResponse = null;
            try {
                errorResponse = gson.fromJson(response.body().string(), BaseResponse.class);
            } catch (Exception ignored) {
            }
            if (errorResponse != null) {
                throw new AppException(gson.toJson(errorResponse));
            } else {
                throw new AppException(context.getString(R.string.network_error));
            }
        }
    }
}
