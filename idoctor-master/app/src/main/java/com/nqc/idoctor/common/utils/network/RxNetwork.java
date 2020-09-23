/*
 * Created by NQC on 4/25/20 8:30 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.utils.network;

import android.content.Context;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.utils.Utils;

import io.reactivex.Single;

public class RxNetwork {

    private RxNetwork() {
    }

    public static Single<Boolean> checkNetwork(Context context) {
        return Single.just(Utils.isNetworkAvailable(context))
                .flatMap(available -> available
                        ? Single.just(true)
                        : Single.error(new NoNetworkException(context.getString(R.string.network_error))));
    }

}
