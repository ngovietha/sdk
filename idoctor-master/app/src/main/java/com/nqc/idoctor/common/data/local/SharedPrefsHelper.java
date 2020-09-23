/*
 * Created by NQC on 4/25/20 8:02 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.data.local;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPrefsHelper
 */
public class SharedPrefsHelper {

    private static final String ACCESS_TOKEN = "_access_token";
    private static final String KEY_BASE_URL = "SharedPrefsHelper.KEY_BASE_URL";
    private static final String KEY_PLAYED_AUDIO = "SharedPrefsHelper.KEY_PLAYED_AUDIO";

    private SharedPreferences mSharedPreferences;

    public SharedPrefsHelper(Context context) {

        mSharedPreferences = context.getSharedPreferences("BaseApp", Context.MODE_PRIVATE);
    }

    public void put(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public void put(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public void put(String key, float value) {
        mSharedPreferences.edit().putFloat(key, value).apply();
    }

    public void put(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public void put(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public String get(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

    public Integer get(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public Float get(String key, float defaultValue) {
        return mSharedPreferences.getFloat(key, defaultValue);
    }

    public Boolean get(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    private void remove(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public void clearAccessToken() {
        remove(ACCESS_TOKEN);
    }

    public String getAccessToken() {
        return get(ACCESS_TOKEN, "");
    }

    public void setBaseUrl(String url) {
        put(KEY_BASE_URL, url);
    }

    public String getBaseUrl() {
        return get(KEY_BASE_URL, null);
    }


    public void firstTimeAskingPermission(String permission, boolean isFirstTime) {
        put(permission, isFirstTime);
    }

    public boolean isFirstTimeAskingPermission(String permission) {
        return get(permission, true);
    }
}
