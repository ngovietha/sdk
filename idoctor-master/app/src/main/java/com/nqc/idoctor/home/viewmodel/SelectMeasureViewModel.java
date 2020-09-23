/*
 * Created by NQC on 5/26/20 7:21 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:31 AM
 *
 */

package com.nqc.idoctor.home.viewmodel;

import android.app.Application;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.viewmodel.BaseViewModel;
import com.nqc.idoctor.home.data.responsitory.HomeRepository;
import com.nqc.idoctor.home.view.callback.HomeCallback;
import com.nqc.idoctor.home.view.navigator.HomeNavigator;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class SelectMeasureViewModel extends BaseViewModel<HomeNavigator> implements HomeCallback {

    private SharedPrefsHelper sharedPrefsHelper;
    private HomeRepository homeRepository;

    @Inject
    public SelectMeasureViewModel(@NonNull Application application,
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
