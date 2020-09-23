/*
 * Created by NQC on 4/25/20 7:30 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/25/20 7:30 PM
 *
 */

package com.nqc.idoctor;

import com.nqc.idoctor.common.di.components.DaggerAppComponent;
import com.nqc.idoctor.common.di.module.ApplicationModule;
import com.nqc.idoctor.common.utils.LogUtils;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;

public class BaseApplication extends DaggerApplication {

    private static BaseApplication sInstance;

    public static BaseApplication getAppContext() {
        return sInstance;
    }

    private static synchronized void setInstance(BaseApplication app) {
        sInstance = app;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.init();

        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable instanceof UndeliverableException) {
                return; // ignore BleExceptions as they were surely delivered at least once
            }
            // add other custom handlers if needed
            throw new RuntimeException("Unexpected Throwable in RxJavaPlugins error handler", throwable);
        });

        setInstance(this);

    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .application(this)
                .applicationModule(new ApplicationModule(this))
                .build();
    }
}
