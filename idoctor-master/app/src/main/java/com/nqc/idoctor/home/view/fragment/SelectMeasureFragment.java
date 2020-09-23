/*
 * Created by NQC on 5/26/20 7:08 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 6:50 AM
 *
 */

package com.nqc.idoctor.home.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.nqc.idoctor.R;
import com.nqc.idoctor.common.view.activity.MainActivity;
import com.nqc.idoctor.common.view.base.BaseFragment;
import com.nqc.idoctor.databinding.FragmentSelectMeasureBinding;
import com.nqc.idoctor.home.data.local.dao.MeasureType;
import com.nqc.idoctor.home.view.adapter.SelectMeasureAdapter;
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
public class SelectMeasureFragment extends BaseFragment<HomeViewModel, FragmentSelectMeasureBinding>
        implements HomeNavigator {

    public static final String TAG = SelectMeasureFragment.class.getSimpleName();

    public static SelectMeasureFragment newInstance() {
        Bundle args = new Bundle();
        SelectMeasureFragment fragment = new SelectMeasureFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initView() {
        ((MainActivity) Objects.requireNonNull(getActivity()))
                .setupActionBar(getString(R.string.select_measure), true);
        setHasOptionsMenu(true);

        dataBinding.setCallback(viewModel);
        dataBinding.setViewModel(viewModel);
        viewModel.setNavigator(this);

        createRecyclerView();
    }

    private void createRecyclerView() {
        List<MeasureType> measureTypes = createTempData();
        SelectMeasureAdapter selectMeasureAdapter = new SelectMeasureAdapter(getContext(), () -> {
            ((MainActivity) Objects.requireNonNull(getActivity()))
                    .goToMeasure();
        });
        selectMeasureAdapter.setData(measureTypes);

        dataBinding.rvHealthStatus.setLayoutManager(
                new GridLayoutManager(getContext(), 2,
                        GridLayoutManager.VERTICAL, false));
        dataBinding.rvHealthStatus.setAdapter(selectMeasureAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<MeasureType> createTempData() {
        List<MeasureType> measureTypes = new ArrayList<>();

        measureTypes.add(new MeasureType("Đường huyết", R.drawable.ic_water, R.color.colorItem1));
        measureTypes.add(new MeasureType("SP02", R.drawable.ic_heart_with_pulse, R.color.colorItem2));
        measureTypes.add(new MeasureType("Huyết áp", R.drawable.ic_heart, R.color.colorItem3));
        measureTypes.add(new MeasureType("Nhịp tim", R.drawable.ic_ecg, R.color.colorItem4));
        measureTypes.add(new MeasureType("Nhiệt độ", R.drawable.ic_thermometer, R.color.colorItem5));

        return measureTypes;
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
        return R.layout.fragment_select_measure;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        observeViewModel(viewModel);
    }

    private void observeViewModel(HomeViewModel viewModel) {

    }
}
