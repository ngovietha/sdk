/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.builder;

import com.nqc.idoctor.common.view.activity.MainActivity;
import com.nqc.idoctor.home.view.activity.WebViewActivity;

import androidx.appcompat.view.WindowCallbackWrapper;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * ActivityBuilderModule for setting dagger
 */
@Module
public abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector(modules = FragmentBuilderModule.class)
    abstract WebViewActivity webViewActivity();
}
