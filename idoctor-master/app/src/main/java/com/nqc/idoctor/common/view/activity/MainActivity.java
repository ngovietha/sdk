/*
 * Created by NQC on 4/26/20 1:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/25/20 7:26 PM
 *
 */

package com.nqc.idoctor.common.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nqc.idoctor.R;
import com.nqc.idoctor.common.utils.FragmentUtils;
import com.nqc.idoctor.common.utils.ToastUtils;
import com.nqc.idoctor.common.view.base.BaseActivity;
import com.nqc.idoctor.common.viewmodel.MainViewModel;
import com.nqc.idoctor.databinding.ActivityMainBinding;
import com.nqc.idoctor.home.view.activity.WebViewActivity;
import com.nqc.idoctor.home.view.fragment.HomeFragment;
import com.nqc.idoctor.home.view.fragment.SelectMeasureFragment;
import com.nqc.idoctor.home.view.fragment.WebViewFragment;
import com.nqc.idoctor.measure.view.fragment.MeasureFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends BaseActivity<MainViewModel, ActivityMainBinding>
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int CODE_ON_BACK = 1;
    public static final String URL_CALL = "https://vr-conf-1.dynu.com/webapp/conference/meet.lien?callType=video&pin=123456&name=Lien-auto-FF";

    @Override
    protected Class<MainViewModel> getViewModel() {
        return MainViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toBeShownOnLockScreen();
    }

    public void setupActionBar(String title, boolean isShowBack) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(isShowBack);
        }
    }

    @Override
    protected void initView() {
        initFragment();
    }

    private void initFragment() {
        goToHome();
        dataBinding.bnvMain.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_ON_BACK) {
            if (resultCode == RESULT_OK) {
                //handle highlight Home tab
                dataBinding.bnvMain.setSelectedItemId(R.id.menu_home);
            }
        }
    }

    private void goToHome() {
        FragmentUtils.replaceFragment(this,
                HomeFragment.newInstance(),
                R.id.fragment_container, false,
                FragmentUtils.TRANSITION_NONE);
    }

    public void goToHomePage(String url) {
        FragmentUtils.replaceFragment(this,
                WebViewFragment.newInstance(url),
                R.id.fragment_container, true,
                FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
    }

    public void goToHomePage() {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        startActivityForResult(intent, CODE_ON_BACK);
    }

    public void goToSelectMeasure() {
        FragmentUtils.replaceFragment(this,
                SelectMeasureFragment.newInstance(),
                R.id.fragment_container, true,
                FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
    }

    public void goToMeasure() {
        FragmentUtils.replaceFragment(this,
                MeasureFragment.newInstance(),
                R.id.fragment_container, true,
                FragmentUtils.TRANSITION_SLIDE_LEFT_RIGHT);
    }

    private void toBeShownOnLockScreen() {
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setTurnScreenOn(true);
            setShowWhenLocked(true);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                            | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                goToHome();
                break;
            case R.id.menu_call:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED
                        || ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 1);
                } else {
                    goToHomePage();
                }


                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToHomePage();
            } else {
                ToastUtils.showToast(this, getString(R.string.message_permissions_denied));
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
            return;
        }
    }
}
