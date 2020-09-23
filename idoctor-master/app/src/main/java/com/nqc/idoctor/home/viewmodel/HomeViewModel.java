/*
 * Created by NQC on 5/26/20 5:06 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:06 AM
 *
 */

package com.nqc.idoctor.home.viewmodel;

import android.app.Application;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.data.remote.response.BaseResponse;
import com.nqc.idoctor.common.viewmodel.BaseViewModel;
import com.nqc.idoctor.home.data.responsitory.HomeRepository;
import com.nqc.idoctor.home.view.callback.HomeCallback;
import com.nqc.idoctor.home.view.navigator.HomeNavigator;
import com.nqc.idoctor.measure.data.responsitory.MeasureRepository;
import com.nqc.idoctor.measure.view.callback.MeasureCallback;
import com.nqc.idoctor.measure.view.navigator.MeasureNavigator;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class HomeViewModel extends BaseViewModel<HomeNavigator> implements HomeCallback {

    private SharedPrefsHelper sharedPrefsHelper;
    private HomeRepository homeRepository;

    @Inject
    public HomeViewModel(@NonNull Application application,
                         HomeRepository homeRepository,
                         SharedPrefsHelper sharedPrefsHelper) {
        super(application);
        this.sharedPrefsHelper = sharedPrefsHelper;
        this.homeRepository = homeRepository;
    }

    @Override
    protected void onTimeOutError() {
        setIsLoading(false);
    }
}
