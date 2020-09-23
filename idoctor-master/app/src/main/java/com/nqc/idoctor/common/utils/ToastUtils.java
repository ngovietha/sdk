/*
 * Created by NQC on 4/25/20 9:01 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast Utils
 */
public class ToastUtils {

  private static Toast sToast;

  public static void showToast(Context context, String message) {
    if (sToast != null) {
      sToast.cancel();
    }
    sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
    sToast.show();
  }
}
