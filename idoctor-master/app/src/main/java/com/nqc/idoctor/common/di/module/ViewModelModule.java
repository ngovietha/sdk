/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.module;

import com.nqc.idoctor.common.utils.ViewModelFactory;
import com.nqc.idoctor.common.viewmodel.MainViewModel;
import com.nqc.idoctor.home.viewmodel.HomeViewModel;
import com.nqc.idoctor.home.viewmodel.SelectMeasureViewModel;
import com.nqc.idoctor.home.viewmodel.WebViewModel;
import com.nqc.idoctor.measure.viewmodel.MeasureViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * ViewModelModule
 */
@Module
public abstract class ViewModelModule {


    @Binds
    @SuppressWarnings("unused")
    abstract ViewModelProvider.Factory bindsViewModelFactory(ViewModelFactory viewModelFactory);

    @Binds
    @IntoMap
    @ViewModelKey(MeasureViewModel.class)
    abstract ViewModel bindsHomeFragmentViewModel(MeasureViewModel homeFragmentViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindsMainActivityViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(WebViewModel.class)
    abstract ViewModel bindsWebViewModel(WebViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    abstract ViewModel bindsHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectMeasureViewModel.class)
    abstract ViewModel bindsSelectMeasureViewModel(SelectMeasureViewModel viewModel);
}
