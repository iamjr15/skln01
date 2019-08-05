package com.autohub.skln.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentRequestDetailBinding;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-05.
 */
public class FragmentRequestDetail extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity)requireActivity()).setStatusBarColor(R.drawable.purple_header);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentRequestDetailBinding binding = FragmentRequestDetailBinding.bind(view);
    }
}
