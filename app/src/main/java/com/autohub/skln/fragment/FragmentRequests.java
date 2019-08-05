package com.autohub.skln.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autohub.skln.FrameActivity;
import com.autohub.skln.R;
import com.autohub.skln.adapters.RequestAdapter;
import com.autohub.skln.databinding.FragmentRequestsBinding;
import com.autohub.skln.listeners.ItemClickListener;
import com.autohub.skln.models.Request;
import com.autohub.skln.utills.AppConstants;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-05.
 */
public class FragmentRequests extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentRequestsBinding binding = FragmentRequestsBinding.bind(view);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setEmptyView(binding.noData);
        binding.recyclerView.setAdapter(new RequestAdapter(requireContext(), new ItemClickListener<Request>() {
            @Override
            public void onClick(Request request) {
                Intent intent = new Intent(requireContext(), FrameActivity.class);
                intent.putExtra(AppConstants.KEY_FRAGMENT, FragmentRequestDetail.class.getName());
                intent.putExtra(AppConstants.KEY_THEME, R.style.AppTheme_NoActionBar_FrameActivity_Purple);
                startActivity(intent);
            }
        }));
    }
}
