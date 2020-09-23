/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.components;

import android.app.Application;

import com.nqc.idoctor.BaseApplication;
import com.nqc.idoctor.common.di.builder.ActivityBuilderModule;
import com.nqc.idoctor.common.di.module.ApplicationModule;
import com.nqc.idoctor.common.di.module.FragmentModule;
import com.nqc.idoctor.common.di.module.RepositoryModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Singleton
@Component(modules = {
        RepositoryModule.class,
        FragmentModule.class,
        AndroidInjectionModule.class,
        ActivityBuilderModule.class,
        ApplicationModule.class
})
public interface AppComponent extends AndroidInjector<BaseApplication> {

    void inject(BaseApplication communicationGroupApp);

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        Builder applicationModule(ApplicationModule applicationModule);

        AppComponent build();
    }
}