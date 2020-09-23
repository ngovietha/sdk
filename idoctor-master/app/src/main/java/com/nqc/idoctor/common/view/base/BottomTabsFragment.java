/*
 * Created by NQC on 4/26/20 1:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.view.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.nqc.idoctor.common.data.local.SharedPrefsHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

/**
 * BottomTabsFragment
 */
public abstract class BottomTabsFragment<V extends ViewModel, D extends ViewDataBinding> extends BaseFragment<V, D> {

    protected SharedPrefsHelper sharedPrefsHelper;
    private FragmentNavigation fragmentNavigation = null;
    private boolean isViewCreated = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentNavigation) {
            fragmentNavigation = (FragmentNavigation) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (fragmentNavigation != null) {
            fragmentNavigation.onActivityCreated(this);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewInitialized(view, savedInstanceState, isViewCreated);
        if (!isViewCreated) {
            isViewCreated = true;
        }
    }

    @Nullable
    @Override
    public View getView() {
        View view = super.getView();
        return view != null ? view : dataBinding.getRoot();
    }

    protected void push(Fragment fragment) {
        if (fragmentNavigation != null) {
            fragmentNavigation.pushFragment(fragment);
        }
    }

    protected void pop() {
        if (fragmentNavigation != null) {
            fragmentNavigation.popFragment();
        }
    }

    public void replace(Fragment fragment) {
        if (fragmentNavigation != null) {
            fragmentNavigation.replaceFragment(fragment);
        }
    }

    protected void finish() {
        pop();
    }

    protected abstract void onViewInitialized(View view, Bundle savedInstanceState, Boolean
            isViewCreated);

    public interface FragmentNavigation {

        void onActivityCreated(Fragment fragment);

        void pushFragment(Fragment fragment);

        void popFragment();

        void replaceFragment(Fragment fragment);
    }
}
