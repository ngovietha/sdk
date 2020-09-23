/*
 * Created by NQC on 5/27/20 6:27 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/27/20 6:27 AM
 *
 */

package com.nqc.idoctor.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.nqc.idoctor.BaseApplication;
import com.nqc.idoctor.common.data.local.SharedPrefsHelper;

import androidx.core.app.ActivityCompat;

public class PermissionUtil {
    /**
     * Check if version is marshmallow and above.
     * Used in deciding to ask runtime permission
     */
    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }


    public static void checkPermission(Context context,
                                       String permission,
                                       PermissionAskListener listener,
                                       SharedPrefsHelper sharedPrefsHelper) {

        boolean isPermission = sharedPrefsHelper.isFirstTimeAskingPermission(permission);
        //If permission is not granted
        if (shouldAskPermission(context, permission)) {
            //If permission denied previously
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {

                listener.onPermissionPreviouslyDenied();
            } else {
                //Permission denied or first time requested
                if (isPermission) {
                    sharedPrefsHelper.firstTimeAskingPermission(permission, false);
                    listener.onNeedPermission();
                } else {
                    //Handle the feature without permission or ask user to manually allow permission
                    listener.onPermissionDisabled();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }

    /**
     * Callback on various cases on checking permission
     * <p>
     * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
     * If permission is already granted, onPermissionGranted() would be called.
     * <p>
     * 2.  Above M, if the permission is being asked first time onNeedPermission() would be called.
     * <p>
     * 3.  Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
     * would be called.
     * <p>
     * 4.  Above M, if the permission is disabled by device policy or the user checked "Never ask again"
     * check box on previous request permission, onPermissionDisabled() would be called.
     */
    public interface PermissionAskListener {
        //Callback to ask permission
        void onNeedPermission();

        //Callback on permission denied
        void onPermissionPreviouslyDenied();

        //Callback on permission "Never show again" checked and denied
        void onPermissionDisabled();

        //Callback on permission granted
        void onPermissionGranted();
    }
}