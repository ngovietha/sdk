/*
 * Created by NQC on 5/26/20 5:06 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:06 AM
 *
 */

package com.nqc.idoctor.home.viewmodel;

import android.app.Application;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.viewmodel.BaseViewModel;
import com.nqc.idoctor.measure.view.navigator.MeasureNavigator;

import javax.inject.Inject;

import androidx.annotation.NonNull;

public class WebViewModel extends BaseViewModel<MeasureNavigator> {

    private SharedPrefsHelper sharedPrefsHelper;

    @Inject
    public WebViewModel(@NonNull Application application,
                        SharedPrefsHelper sharedPrefsHelper) {
        super(application);

        this.sharedPrefsHelper = sharedPrefsHelper;
    }

    @Override
    protected void onTimeOutError() {

    }
}
