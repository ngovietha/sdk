/*
 * Created by NQC on 4/25/20 8:30 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.utils.network;

import android.content.Context;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.utils.ToastUtils;

public class NetworkUtils {

    public static void showToastNetworkError(Context context, Throwable throwable) {
        if (throwable instanceof NoNetworkException) {
            ToastUtils.showToast(context, context.getString(R.string.network_error));
        }
    }

    public static boolean isNetWorkException(Throwable throwable) {
        return throwable instanceof NoNetworkException;
    }
}
