/*
 * Created by NQC on 5/26/20 5:06 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:06 AM
 *
 */

package com.nqc.idoctor.home.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.utils.BundleUtils;
import com.nqc.idoctor.common.view.base.BaseFragment;
import com.nqc.idoctor.databinding.FragmentWebViewBinding;
import com.nqc.idoctor.home.viewmodel.WebViewModel;

import androidx.annotation.Nullable;

/**
 * LoginFragment screen
 */
public class WebViewFragment extends BaseFragment<WebViewModel, FragmentWebViewBinding> {

    public static final String TAG = WebViewFragment.class.getSimpleName();
    private static final String KEY_URL = "WebViewFragment.KEY_URL";
    private String url;


    public static WebViewFragment newInstance(String url) {
        Bundle args = new Bundle();
        WebViewFragment fragment = new WebViewFragment();
        args.putString(KEY_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected Class<WebViewModel> getViewModel() {
        return WebViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_web_view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel(viewModel);
    }

    private void observeViewModel(WebViewModel viewModel) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        url = BundleUtils.getString(getArguments(), KEY_URL, "");
        goUrl();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void goUrl() {
        dataBinding.wvHomePage.getSettings().setLoadsImagesAutomatically(true);
        dataBinding.wvHomePage.getSettings().setJavaScriptEnabled(true);
        dataBinding.wvHomePage.getSettings().setDomStorageEnabled(true);
        dataBinding.wvHomePage.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        dataBinding.wvHomePage.setScrollbarFadingEnabled(true);
        dataBinding.wvHomePage.getSettings().setLoadWithOverviewMode(true);
        dataBinding.wvHomePage.setInitialScale(100);

        dataBinding.wvHomePage.loadUrl(url);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
