/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.network;

import android.content.Context;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.utils.network.AppException;

public class SocketHelper {

  public static void handleSocketHelper(Context context) {
    throw new AppException(context.getString(R.string.system_error));
  }
}
