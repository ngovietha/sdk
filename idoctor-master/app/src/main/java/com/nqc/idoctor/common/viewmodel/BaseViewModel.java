/*
 * Created by NQC on 4/26/20 2:38 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.viewmodel;


import android.app.Application;
import android.os.Handler;

import com.nqc.idoctor.common.data.remote.ApiConstants;
import com.nqc.idoctor.common.utils.SingleLiveEvent;

import java.lang.ref.WeakReference;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import io.reactivex.disposables.CompositeDisposable;

/**
 * BaseViewModel
 */
public abstract class BaseViewModel<N> extends AndroidViewModel {

    private SingleLiveEvent<Boolean> isLoading;
    protected CompositeDisposable compositeDisposable;
    private WeakReference<N> navigator;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        compositeDisposable = new CompositeDisposable();
        isLoading = new SingleLiveEvent<>();
    }

    public SingleLiveEvent<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.postValue(isLoading);
        if (isLoading) {
            new Handler().postDelayed(() -> {
                if (getIsLoading().getValue()) {
                    setIsLoading(false);
                    onTimeOutError();
                }
            }, ApiConstants.CONNECT_TIMEOUT);
        }
    }

    protected abstract void onTimeOutError();

    @Override
    protected void onCleared() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        super.onCleared();
    }

    public N getNavigator() {
        return navigator.get();
    }

    public void setNavigator(N navigator) {
        this.navigator = new WeakReference<>(navigator);
    }
}