/*
 * Created by NQC on 4/25/20 8:04 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.remote;

import com.nqc.idoctor.AppConfig;
import com.nqc.idoctor.common.data.local.SharedPrefsHelper;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * service setting  AuthorizationInterceptor
 */
public class AuthorizationInterceptor implements Interceptor {

    private SharedPrefsHelper appPrefs;

    public AuthorizationInterceptor(SharedPrefsHelper appPrefs) {
        this.appPrefs = appPrefs;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (!appPrefs.getAccessToken().isEmpty()) {
            builder.addHeader(AppConfig.API_AUTH_HEADER_KEY_NAME, appPrefs.getAccessToken());
        }
        return chain.proceed(builder.build());
    }
}
