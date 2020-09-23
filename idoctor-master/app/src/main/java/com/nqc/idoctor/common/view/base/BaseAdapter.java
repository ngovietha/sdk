/*
 * Created by NQC on 4/26/20 1:04 AM
 *  Copyright© 2020 NQC. All Rights Reserved.
 *  Last modified 4/21/20 9:28 PM
 *
 */

package com.nqc.idoctor.common.view.base;


import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * BaseAdapter
 *
 * @param <T>
 * @param <D>
 */
public abstract class BaseAdapter<T extends RecyclerView.ViewHolder, D> extends RecyclerView.Adapter<T> {

    public abstract void setData(List<D> data);
}