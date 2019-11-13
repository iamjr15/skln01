package com.netzwelt.studentmodule.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.autohub.skln.listeners.ItemClickListener;
import com.autohub.skln.models.RequestViewModel;
import com.netzwelt.studentmodule.databinding.ItemRequestBinding;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vt Netzwelt
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
        holder.mBinding.ll.setOnClickListener(mOnClickListener);
        holder.mBinding.ll.setTag(position);
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
