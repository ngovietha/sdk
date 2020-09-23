/*
 * Created by NQC on 4/25/20 8:04 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.remote;

import com.nqc.idoctor.BaseApplication;
import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * RequestInterceptor
 */
public class RequestInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPrefsHelper appPrefs = new SharedPrefsHelper(BaseApplication.getAppContext());
        Request originalRequest = chain.request();

        String newUrlString = Utils.getBaseURL(appPrefs);
        URL newUrl = new URL(newUrlString);


        String currentHost = originalRequest.url().host();
        String currentPort = originalRequest.url().port() == -1 ?
                "" : String.valueOf(originalRequest.url().port());

        String currentUrl = originalRequest.url().url().toString();

        currentUrl = currentUrl.replace(currentHost, newUrl.getHost())
                .replace(currentPort,
                        newUrl.getPort() == -1 ? "" : String.valueOf(newUrl.getPort()));


        HttpUrl newHttpUrl = HttpUrl.parse(currentUrl);

        originalRequest = originalRequest.newBuilder()
                .url(newHttpUrl)
                .build();
        return chain.proceed(originalRequest);
    }
}