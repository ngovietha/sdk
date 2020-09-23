/*
 * Created by NQC on 4/25/20 8:34 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nqc.idoctor.AppConfig;
import com.nqc.idoctor.BuildConfig;
import com.nqc.idoctor.R;
import com.nqc.idoctor.common.data.local.SharedPrefsHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    private static final String FORMAT_DATE = "dd:MM:yyyy";
    private static final String FORMAT_DATE_TIME = "dd:MM:yyyy HH:mm:ss";
    private static final DateFormat df = DateFormat.getDateInstance(DateFormat.FULL, new Locale("vn"));

    /**
     * hideAllKeyboard
     */
    public static void hideAllKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    /**
     * isNetworkAvailable
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        return true;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        ToastUtils.showToast(context, context.getString(R.string.network_error));
        return false;
    }

    /**
     * isBlank
     */
    public static boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * loadImage
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
//                .error(R.drawable.ic_no_img)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * convertDateToString
     */
    @SuppressLint("SimpleDateFormat")
    public String convertDateToString(Date date, String format) {

        String dateStr = null;
        if (format == null) {
            format = FORMAT_DATE;
        }
        DateFormat df;
        df = new SimpleDateFormat(format);

        try {
            dateStr = df.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dateStr;
    }

    public static int parserStringToInt(String value, int _default) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return _default;
        }
    }

    /**
     * calculate available number of columns in GridView
     */
    public static int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    }

    /**
     * get URL from Id of modality
     */
    public static String getHomeUrlFromId(SharedPrefsHelper prefsHelper, String id) {
        if (prefsHelper == null) return "";
        return getBaseURL(prefsHelper) + "/ris/modality/" + id + "/livescreen";
    }

    /**
     * get BASE URL
     */
    public static String getBaseURL(SharedPrefsHelper prefsHelper) {
        if (prefsHelper == null) return "";
        return prefsHelper.getBaseUrl() == null ? AppConfig.BASE_URL : prefsHelper.getBaseUrl();
    }

    /**
     * get Speaker URL
     */
    public static String getSpeakerUrl(SharedPrefsHelper sharedPrefsHelper, String content) {
        if (sharedPrefsHelper == null) return "";
        return getBaseURL(sharedPrefsHelper) + "/ris/rest/forwardUrlSpeaker?" + content;
    }

    /**
     * check String is URL or not
     */
    public static boolean isUrl(String s) {
        try {
            new URL(s);
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }
}
