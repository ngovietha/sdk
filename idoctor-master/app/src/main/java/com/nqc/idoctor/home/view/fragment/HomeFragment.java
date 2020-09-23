/*
 * Created by NQC on 5/26/20 5:06 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:06 AM
 *
 */

package com.nqc.idoctor.home.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.view.activity.MainActivity;
import com.nqc.idoctor.common.view.base.BaseFragment;
import com.nqc.idoctor.databinding.FragmentHomeBinding;
import com.nqc.idoctor.home.data.local.dao.HealthStatus;
import com.nqc.idoctor.home.view.adapter.HealthStatusAdapter;
import com.nqc.idoctor.home.view.navigator.HomeNavigator;
import com.nqc.idoctor.home.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * LoginFragment screen
 */
public class HomeFragment extends BaseFragment<HomeViewModel, FragmentHomeBinding>
        implements HomeNavigator, View.OnClickListener {

    public static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .setupActionBar(getString(R.string.home_page), false);
        dataBinding.setCallback(viewModel);
        dataBinding.setViewModel(viewModel);
        viewModel.setNavigator(this);

        createRecyclerView();

        dataBinding.fabMeasure.setOnClickListener(this);
    }

    private void createRecyclerView() {
        List<HealthStatus> healthStatuses = createTempData();
        HealthStatusAdapter healthStatusAdapter = new HealthStatusAdapter(getContext());
        healthStatusAdapter.setData(healthStatuses);

        dataBinding.rvHealthStatus.setLayoutManager(
                new GridLayoutManager(getContext(), 2,
                        GridLayoutManager.VERTICAL, false));
        dataBinding.rvHealthStatus.setAdapter(healthStatusAdapter);

    }

    private List<HealthStatus> createTempData() {
        List<HealthStatus> healthStatuses = new ArrayList<>();

        healthStatuses.add(new HealthStatus(R.drawable.ic_water, "Đường huyết",
                "6.8",
                "Đo lúc 10:30 ngày 21/5"));

        healthStatuses.add(new HealthStatus(R.drawable.ic_heart_with_pulse, "SP02",
                "98",
                "Đo lúc 10:36 ngày 21/5"));

        healthStatuses.add(new HealthStatus(R.drawable.ic_heart, "Huyết áp",
                "105/80",
                "Đo lúc 11:00 ngày 21/5"));

        healthStatuses.add(new HealthStatus(R.drawable.ic_ecg, "Nhịp tim",
                "90",
                "Đo lúc 11:12 ngày 21/5"));

        healthStatuses.add(new HealthStatus(R.drawable.ic_thermometer, "Nhiệt độ",
                "36.6",
                "Đo lúc 11:15 ngày 21/5"));

        return healthStatuses;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        return dataBinding.getRoot();
    }

    @Override
    protected Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel(viewModel);
    }

    private void observeViewModel(HomeViewModel viewModel) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabMeasure:
                ((MainActivity) Objects.requireNonNull(getActivity())).goToSelectMeasure();
                break;
        }
    }
}
