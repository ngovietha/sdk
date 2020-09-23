/*
 * Created by NQC on 4/26/20 1:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.view.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.utils.LogUtils;
import com.nqc.idoctor.common.utils.Utils;

import javax.inject.Inject;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.DaggerFragment;
import io.reactivex.disposables.CompositeDisposable;

/**
 * BaseFragment
 */
public abstract class BaseFragment<V extends ViewModel, D extends ViewDataBinding> extends DaggerFragment {

    private CompositeDisposable compositeDisposable;
    protected V viewModel;
    protected D dataBinding;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    protected SharedPrefsHelper appPrefs;

    protected abstract Class<V> getViewModel();

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();

        viewModel = new ViewModelProvider(this, viewModelFactory).get(getViewModel());
    }

    @Override
    public void onResume() {
        super.onResume();
//        LogUtils.d(getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (dataBinding == null) {
            dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        }
        return dataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        if (getActivity() != null) {
            Utils.hideAllKeyboard(getActivity());
        }

        super.onDestroyView();
    }

    protected void onBackPressed() {
        if (getActivity() != null) {
            getActivity().onBackPressed();
        }
    }

    public abstract void initView();

}
