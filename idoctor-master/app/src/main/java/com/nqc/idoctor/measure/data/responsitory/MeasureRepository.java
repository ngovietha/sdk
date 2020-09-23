/*
 * Created by NQC on 4/26/20 10:27 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.measure.data.responsitory;

import android.content.Context;

import com.nqc.idoctor.common.data.local.BaseDatabase;
import com.nqc.idoctor.common.data.remote.response.BaseResponse;
import com.nqc.idoctor.common.utils.network.RxNetwork;
import com.nqc.idoctor.measure.data.remote.service.MeasureService;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

/**
 * Account Repository call api to server
 */
public class MeasureRepository {

    private MeasureService service;
    private Context mContext;
    private BaseDatabase baseDatabase;

    @Inject
    public MeasureRepository(Context mContext, MeasureService service, BaseDatabase baseDatabase) {
        this.service = service;
        this.mContext = mContext;
        this.baseDatabase = baseDatabase;
    }

    public Single<BaseResponse> postECGData(String ecgData) {
        return RxNetwork.checkNetwork(mContext)
                .flatMap(connected -> service.postECGData(ecgData))
                .subscribeOn(Schedulers.io());
    }
}



