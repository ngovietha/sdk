/*
 * Created by NQC on 4/26/20 2:44 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.utils;

import android.os.Bundle;

/**
 * Class Bundle Utils Execute get data from Bundle
 */
public class BundleUtils {

  public static int getInt(Bundle bundle, String key, int defaultValue) {
    return bundle != null ? bundle.getInt(key, defaultValue) : defaultValue;
  }

  public static long getLong(Bundle bundle, String key, long defaultValue) {
    return bundle != null ? bundle.getLong(key, defaultValue) : defaultValue;
  }

  public static boolean getBoolean(Bundle bundle, String key, boolean defaultValue) {
    return bundle != null ? bundle.getBoolean(key, defaultValue) : defaultValue;
  }

  public static String getString(Bundle bundle, String key, String defaultValue) {
    return bundle != null ? bundle.getString(key, defaultValue) : defaultValue;
  }
}
