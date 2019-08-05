package com.autohub.skln.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentClassRequestsBinding;
import com.autohub.skln.utills.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class FragmentClassRequests extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentClassRequestsBinding mBinding = FragmentClassRequestsBinding.bind(view);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addData(getFragmentClassRequests("Latest"), "Latest");
        adapter.addData(getFragmentClassRequests("All"), "All");
        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
        mBinding.viewpager.setAdapter(adapter);
    }

    private FragmentRequests getFragmentClassRequests(String type) {
        FragmentRequests latestRequests = new FragmentRequests();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.KEY_TYPE, type);
        return latestRequests;
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Info> mData = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mData.get(position).fragment;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        void addData(Fragment fragment, String title) {
            mData.add(new Info(fragment, title));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mData.get(position).title;
        }

        class Info {
            final Fragment fragment;
            final String title;

            Info(Fragment fragment, String title) {
                this.fragment = fragment;
                this.title = title;
            }
        }
    }
}