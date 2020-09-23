/*
 * Created by NQC on 4/25/20 7:52 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.utils;

import com.nqc.idoctor.BuildConfig;

import java.util.ArrayList;

import timber.log.Timber;

public class LogUtils {

    public static final String TAG = LogUtils.class.getSimpleName();

    public static void init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static void d(String message) {
        try {
            Timber.d(getCallerClassName() + " : " + message);
        } catch (Exception e) {
            Timber.d(getCallerClassName() + " : " + message);
        }
    }

    public static void d(String message, Throwable t) {
        try {

            Timber.d(t, getCallerClassName() + " : " + message);
        } catch (Exception e) {
            Timber.d(t, getCallerClassName() + " : " + message);
        }
    }

    public static void enter() {
        d("");
    }

    private static String getCallerClassName() {

        ArrayList<String> data = new ArrayList<>();

        data.add(LogUtils.class.getName());

        data.add("java.lang.reflect.Method");
        data.add("java.lang.Thread");

        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            String name = ste.getClassName();
            String method = ste.getMethodName();
            int line = ste.getLineNumber();

            if (!isAndroid(name) && !data.contains(name)) {
                if (name.contains(".")) {
                    name = name.substring(name.lastIndexOf(".") + 1);
                }
                return String.format("%s.(%s.java:%s)", method, name, line);
            }

        }

        return "";
    }

    private static boolean isAndroid(String name) {
        return name.startsWith("com.android") || name.startsWith("android") || name.startsWith("androidx");
    }
}
