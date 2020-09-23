/*
 * Created by NQC on 4/26/20 1:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.view.base;

import android.os.Bundle;
import android.view.MenuItem;

import com.ncapdevi.fragnav.FragNavController;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

/**
 * BottomTabsActivity
 */
public abstract class BottomTabsActivity<V extends ViewModel, D extends ViewDataBinding> extends
        BaseActivity<V, D> implements BottomTabsFragment.FragmentNavigation, FragNavController.TransactionListener,
        FragNavController.RootFragmentListener {

    protected FragNavController navController = null;
    protected abstract void onFragmentChanged(Fragment fragment, Boolean isRootFragment);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (navController == null) {
            super.onBackPressed();
            return;
        }
        if (navController.isRootFragment()) {
            if (!handleBackPress()) {
                super.onBackPressed();
                return;
            }
        }
        navController.popFragment();
    }

    protected abstract Boolean handleBackPress();

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navController != null) {
            navController.onSaveInstanceState(outState);
        }
    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (navController != null) {
            navController.pushFragment(fragment);
        }
    }

    @Override
    public void popFragment() {
        if (navController != null) {
            navController.popFragment();
        }
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        if (navController != null && fragment != navController.getCurrentFrag()) {
            navController.replaceFragment(fragment);
        }
    }

    @Override
    public void onActivityCreated(Fragment fragment) {
        if (navController != null) {
            onFragmentChanged(fragment, navController.isRootFragment());
        }
    }

    @Override
    public void onTabTransaction(@Nullable Fragment fragment, int i) {
        if (navController != null) {
            onFragmentChanged(fragment, navController.isRootFragment());
        }
    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {
        if (navController != null) {
            onFragmentChanged(fragment, navController.isRootFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (navController != null && item.getItemId() == android.R.id.home) {
            navController.popFragment();
        }
        return true;
    }

}
