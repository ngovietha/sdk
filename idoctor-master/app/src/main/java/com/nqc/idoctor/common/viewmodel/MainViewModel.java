/*
 * Created by NQC on 4/26/20 2:38 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.viewmodel;

import android.app.Application;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;

import javax.inject.Inject;

import androidx.annotation.NonNull;

/**
 * ActivityViewModel
 */
public class MainViewModel extends BaseViewModel {

    private SharedPrefsHelper sharedPrefsHelper;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    protected void onTimeOutError() {

    }
}
