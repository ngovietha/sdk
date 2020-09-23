/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.module;

import com.nqc.idoctor.home.view.fragment.HomeFragment;
import com.nqc.idoctor.home.view.fragment.SelectMeasureFragment;
import com.nqc.idoctor.home.view.fragment.WebViewFragment;
import com.nqc.idoctor.measure.view.fragment.MeasureFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * FragmentModule for setting dagger
 */
@Module
public class FragmentModule {

    @Provides
    @Singleton
    MeasureFragment provideMeasureFragment() {
        return MeasureFragment.newInstance();
    }

    @Provides
    @Singleton
    WebViewFragment provideWebViewFragment(String url) {
        return WebViewFragment.newInstance(url);
    }

    @Provides
    @Singleton
    HomeFragment provideHomeFragment() {
        return HomeFragment.newInstance();
    }

    @Provides
    @Singleton
    SelectMeasureFragment provideSelectMeasureFragment() {
        return SelectMeasureFragment.newInstance();
    }
}