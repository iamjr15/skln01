package com.autohub.skln.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autohub.skln.databinding.ItemRequestBinding;
import com.autohub.skln.listeners.ItemClickListener;
import com.autohub.skln.models.Request;
import com.autohub.skln.models.RequestViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-05.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.Holder> {
    private final List<RequestViewModel> mData = new ArrayList<>();
    private final LayoutInflater mLayoutInflater;
    private ItemClickListener<RequestViewModel> mItemClickListener;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            mItemClickListener.onClick(mData.get(index));
        }
    };

    public RequestAdapter(Context context, ItemClickListener<RequestViewModel> itemClickListener) {
        mLayoutInflater = LayoutInflater.from(context);
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new Holder(ItemRequestBinding.inflate(mLayoutInflater, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.mBinding.setModel(mData.get(position));
        holder.mBinding.next.setOnClickListener(mOnClickListener);
        holder.mBinding.next.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<RequestViewModel> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    static class Holder extends RecyclerView.ViewHolder {
        ItemRequestBinding mBinding;

        Holder(@NonNull ItemRequestBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
