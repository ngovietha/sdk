/*
 * Created by NQC on 4/26/20 3:16 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/26/20 2:59 AM
 *
 */

package com.nqc.idoctor.measure.viewmodel;

import android.app.Application;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.data.remote.response.BaseResponse;
import com.nqc.idoctor.common.viewmodel.BaseViewModel;
import com.nqc.idoctor.measure.data.responsitory.MeasureRepository;
import com.nqc.idoctor.measure.view.callback.MeasureCallback;
import com.nqc.idoctor.measure.view.navigator.MeasureNavigator;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MeasureViewModel extends BaseViewModel<MeasureNavigator> implements MeasureCallback {

    private SharedPrefsHelper sharedPrefsHelper;
    private MeasureRepository measureRepository;

    @Inject
    public MeasureViewModel(@NonNull Application application,
                            MeasureRepository measureRepository,
                            SharedPrefsHelper sharedPrefsHelper) {
        super(application);
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.measureRepository = measureRepository;
    }

    public void postDataECG(String dataECG) {
        setIsLoading(true);

        compositeDisposable.add(
                measureRepository.postECGData(dataECG)
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(BaseResponse::getData)
                        .subscribe(response -> {
                            if (response != null) {
                                setIsLoading(false);
                            }
                        }, throwable -> setIsLoading(false)));

    }
    @Override
    public void onClickEditBaseUrl() {
        getNavigator().onClickEditBaseUrl();
    }

    @Override
    protected void onTimeOutError() {
        setIsLoading(false);
    }
}
