/*
 * Created by NQC on 4/26/20 1:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.view.base;

import android.os.Bundle;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.utils.LogUtils;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.CompositeDisposable;

/**
 * BaseActivity
 */
public abstract class BaseActivity<V extends ViewModel,
        D extends ViewDataBinding> extends AppCompatActivity implements
        HasSupportFragmentInjector {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected abstract Class<V> getViewModel();

    public D dataBinding;
    protected V viewModel;

    protected CompositeDisposable compositeDisposable;
    @Inject
    protected SharedPrefsHelper appPrefs;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentAndroidInjector;

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        compositeDisposable = new CompositeDisposable();
        viewModel = new ViewModelProvider(this, viewModelFactory).get(getViewModel());
        initView();
    }

    protected abstract void initView();

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        super.onDestroy();
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentAndroidInjector;
    }
}


