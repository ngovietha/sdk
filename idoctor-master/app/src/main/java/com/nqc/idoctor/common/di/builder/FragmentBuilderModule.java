/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.builder;

import com.nqc.idoctor.home.view.fragment.HomeFragment;
import com.nqc.idoctor.home.view.fragment.SelectMeasureFragment;
import com.nqc.idoctor.home.view.fragment.WebViewFragment;
import com.nqc.idoctor.measure.view.fragment.MeasureFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * FragmentBuilderModule for setting dagger
 */
@Module
public abstract class FragmentBuilderModule {

    @ContributesAndroidInjector
    abstract MeasureFragment contributeMeasureFragment();

    @ContributesAndroidInjector
    abstract WebViewFragment contributeWebViewFragment();

    @ContributesAndroidInjector
    abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    abstract SelectMeasureFragment contributeSelectMeasureFragment();
}
