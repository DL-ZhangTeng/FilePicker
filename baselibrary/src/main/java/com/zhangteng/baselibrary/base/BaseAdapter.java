package com.zhangteng.baselibrary.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by swing on 2018/5/7.
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<T> data;
    protected boolean hasHeaderOrFooter = false;

    public BaseAdapter(List<T> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data == null || data.isEmpty() ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public boolean isHasHeaderOrFooter() {
        return hasHeaderOrFooter;
    }

    public void setHasHeaderOrFooter(boolean hasHeaderOrFooter) {
        this.hasHeaderOrFooter = hasHeaderOrFooter;
    }
}
