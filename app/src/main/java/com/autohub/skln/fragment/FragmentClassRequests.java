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
import com.autohub.skln.models.User;
import com.autohub.skln.utills.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentClassRequests extends BaseFragment {
    private String mType = "student";
    private FragmentClassRequestsBinding mBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(AppConstants.KEY_TYPE, "student");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentClassRequestsBinding.bind(view);
        fetchUser();

    }

    private void fetchUser() {
        String root = getString(R.string.db_root_tutors);
        if (mType.equalsIgnoreCase("student")) {
            root = getString(R.string.db_root_students);
        }
        getFirebaseStore().collection(root).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
                        adapter.addData(getFragmentClassRequests("Latest", user), "Latest");
                        adapter.addData(getFragmentClassRequests("All", user), "All");
                        mBinding.tabs.setupWithViewPager(mBinding.viewpager);
                        mBinding.viewpager.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
    }

    private FragmentRequests getFragmentClassRequests(String type, User user) {
        FragmentRequests latestRequests = new FragmentRequests();
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.KEY_TYPE, type);
        bundle.putParcelable(AppConstants.KEY_DATA, user);
//        String root = "Student";
//        if (mType.equalsIgnoreCase("student")) {
//            root = "Tutor";
//        }
        bundle.putString("_user_type", mType);
        latestRequests.setArguments(bundle);
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

        @Override
        public long getItemId(int position) {
            return System.currentTimeMillis();
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