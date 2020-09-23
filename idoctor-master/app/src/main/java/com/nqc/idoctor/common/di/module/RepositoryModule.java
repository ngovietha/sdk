/*
 * Created by NQC on 4/25/20 7:56 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.di.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nqc.idoctor.AppConfig;
import com.nqc.idoctor.BuildConfig;
import com.nqc.idoctor.common.data.local.BaseDatabase;
import com.nqc.idoctor.common.data.local.SharedPrefsHelper;
import com.nqc.idoctor.common.data.remote.ApiConstants;
import com.nqc.idoctor.common.data.remote.AuthorizationInterceptor;
import com.nqc.idoctor.common.data.remote.RequestInterceptor;
import com.nqc.idoctor.common.data.remote.service.BaseService;
import com.nqc.idoctor.common.di.network.NetworkHelper;
import com.nqc.idoctor.common.di.network.SocketHelper;
import com.nqc.idoctor.measure.data.remote.service.MeasureService;

import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * all RepositoryModule for setting dagger
 */
@Module(includes = ViewModelModule.class)
public class RepositoryModule {

    @Provides
    @Singleton
    SharedPrefsHelper provideSharedPrefsHelper(Context context) {
        return new SharedPrefsHelper(context);
    }

    private static Retrofit createRetrofit(OkHttpClient okHttpClient, Gson gson, String baseUrl) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    /**
     * Provides Gson.
     */
    @Singleton
    @Provides
    Gson provideGson() {
        return new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
    }

    /**
     * Provides Retrofit.
     */
    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson, SharedPrefsHelper prefsHelper) {
        return createRetrofit(okHttpClient, gson, AppConfig.BASE_URL);
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(SharedPrefsHelper appPrefs, Context context, Gson gson) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.connectTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.readTimeout(ApiConstants.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.writeTimeout(ApiConstants.WRITE_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.callTimeout(ApiConstants.CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        okHttpClient.retryOnConnectionFailure(false);
        okHttpClient.addInterceptor(new RequestInterceptor());
        okHttpClient.addInterceptor(new AuthorizationInterceptor(appPrefs));
        okHttpClient.addInterceptor(chain -> {
            Response response = null;
            try {
                response = chain.proceed(chain.request());
                if (!response.isSuccessful()) {
                    NetworkHelper.handleNetworkError(context, response, gson);
                }

            } catch (SocketException exception) {
                SocketHelper.handleSocketHelper(context);
            }
            return response;
        });
        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return okHttpClient.build();
    }


    @Provides
    @Singleton
    BaseDatabase provideCommunicationAppDatabase(Application application) {
        return Room.databaseBuilder(application, BaseDatabase.class, "baseDatabase.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    /**
     *
     */
    @Provides
    @Singleton
    BaseService provideRetrofitLogin(Retrofit retrofit) {
        return retrofit.create(BaseService.class);
    }

    @Provides
    @Singleton
    MeasureService provideModalitiesService(Retrofit retrofit) {
        return retrofit.create(MeasureService.class);
    }
}
