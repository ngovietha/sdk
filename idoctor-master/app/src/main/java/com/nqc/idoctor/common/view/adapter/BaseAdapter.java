/*
 * Created by NQC on 4/30/20 10:19 PM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/30/20 10:19 PM
 *
 */

package com.nqc.idoctor.common.view.adapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, D> extends RecyclerView.Adapter<T> {

    public abstract void setData(List<D> data);
}