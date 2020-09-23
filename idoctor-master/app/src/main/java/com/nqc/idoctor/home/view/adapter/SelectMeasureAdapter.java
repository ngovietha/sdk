/*
 * Created by NQC on 5/26/20 7:23 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 5/26/20 6:58 AM
 *
 */

package com.nqc.idoctor.home.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nqc.idoctor.R;
import com.nqc.idoctor.databinding.ItemMeasureBinding;
import com.nqc.idoctor.home.data.local.dao.MeasureType;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SelectMeasureAdapter extends RecyclerView.Adapter<SelectMeasureAdapter.MeasureViewHolder> {
    private List<MeasureType> mesureTypes;
    private Context context;
    private ItemClickListener itemClickListener;

    public SelectMeasureAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    public void setData(List<MeasureType> measureTypes) {
        this.mesureTypes = measureTypes;
    }

    @NonNull
    @Override
    public MeasureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMeasureBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.item_measure, parent, false);
        return new MeasureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MeasureViewHolder holder, int position) {
        MeasureType measureType = mesureTypes.get(position);
        holder.binding.setMeasureType(measureType);
        holder.binding.imvIcon.setImageResource(measureType.getImageRes());
        holder.binding.llContainer.setBackground(context.getResources().getDrawable(measureType.getBackgroundRes()));
        holder.binding.cardView.setOnClickListener(v -> itemClickListener.onItemClickListener());
    }

    @Override
    public int getItemCount() {
        return mesureTypes == null ? 0 : mesureTypes.size();
    }

    public class MeasureViewHolder extends RecyclerView.ViewHolder {

        final ItemMeasureBinding binding;

        public MeasureViewHolder(ItemMeasureBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface ItemClickListener {
        void onItemClickListener();
    }
}
