/*
 * Created by NQC on 5/26/20 5:42 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 5:42 AM
 *
 */

package com.nqc.idoctor.home.view.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nqc.idoctor.R;
import com.nqc.idoctor.databinding.ItemHealthStatusBinding;
import com.nqc.idoctor.home.data.local.dao.HealthStatus;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class HealthStatusAdapter extends RecyclerView.Adapter<HealthStatusAdapter.HealthStatusViewHolder> {
    private List<HealthStatus> healthStatuses;
    private Context context;

    public HealthStatusAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<HealthStatus> healthStatuses) {
        this.healthStatuses = healthStatuses;
    }

    @NonNull
    @Override
    public HealthStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHealthStatusBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_health_status, parent, false);
        return new HealthStatusViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HealthStatusViewHolder holder, int position) {
        HealthStatus healthStatus = healthStatuses.get(position);
        holder.binding.setHealthStatus(healthStatus);
        holder.binding.imvIcon.setImageResource(healthStatus.getImageRes());
    }

    @Override
    public int getItemCount() {
        return healthStatuses == null ? 0 : healthStatuses.size();
    }

    public class HealthStatusViewHolder extends RecyclerView.ViewHolder {

        final ItemHealthStatusBinding binding;

        public HealthStatusViewHolder(ItemHealthStatusBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
