/*
 * Created by NQC on 5/27/20 6:21 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/27/20 6:18 AM
 *
 */

package com.nqc.idoctor.home.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.view.base.BaseActivity;
import com.nqc.idoctor.common.viewmodel.MainViewModel;
import com.nqc.idoctor.databinding.ActivityWebViewBinding;

public class WebViewActivity extends BaseActivity<MainViewModel, ActivityWebViewBinding> {

    public static final String TAG = WebViewActivity.class.getSimpleName();

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setupActionBar(getString(R.string.call), true);

        setupWebView();
    }

    private void setupWebView() {
        dataBinding.webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }
            }
        });
        WebView.setWebContentsDebuggingEnabled(true);

        final WebSettings webSettings = dataBinding.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setPluginState(WebSettings.PluginState.ON);

        dataBinding.webView.setWebViewClient(new WebViewClient() {
            // autoplay when finished loading via javascript injection
            public void onPageFinished(WebView view, String url) {
                dataBinding.webView.loadUrl("javascript:(function() { setTimeout(function(){document.getElementsByTagName('video')[0].play();}, 300);  })()");
                dataBinding.webView.loadUrl("javascript:(function() { setTimeout(function(){document.getElementsByTagName('video')[1].play();}, 300);  })()");
            }
        });
        //myWebView.setWebChromeClient(new WebChromeClient());
        dataBinding.webView.loadUrl("file:///android_asset/webapp/index.html");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackIntent();
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        onBackIntent();
        super.onBackPressed();
    }

    private void onBackIntent() {
        Intent data = new Intent();
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onPause() {
        dataBinding.webView.onPause();
        dataBinding.webView.pauseTimers();
        super.onPause();
    }

    @Override
    protected void onResume() {
        dataBinding.webView.resumeTimers();
        dataBinding.webView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        dataBinding.webView.destroy();
        super.onDestroy();
    }

    public void setupActionBar(String title, boolean isShowBack) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(isShowBack);
        }
    }
}
