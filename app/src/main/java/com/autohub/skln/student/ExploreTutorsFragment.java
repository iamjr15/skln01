package com.autohub.skln.student;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.autohub.skln.R;
import com.autohub.skln.databinding.ExploreTutorFragmentBinding;
import com.autohub.skln.databinding.ItemExploreTeacherBinding;
import com.autohub.skln.databinding.ItemSubjectBinding;
import com.autohub.skln.fragment.BaseFragment;
import com.autohub.skln.fragment.FullProfileTutorFragment;
import com.autohub.skln.models.User;
import com.autohub.skln.models.UserViewModel;
import com.autohub.skln.utills.ActivityUtils;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.GlideApp;
import com.autohub.skln.utills.GpsUtils;
import com.autohub.skln.utills.LocationProvider;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-06.
 */
public class ExploreTutorsFragment extends BaseFragment {
    private ExploreTutorFragmentBinding mBinding;
    @Nullable
    private Location mCurrentLocation;
    private GpsUtils mGpsUtils;
    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            Log.d(">>>>Location", location.toString());
//            LocationProvider.getInstance().getAddressFromLocation(container.getContext(), location, new AddressListener() {
//                @Override
//                public void onAddressDecoded(String address) {
//                    Log.d(">>>>LocationAddress","Address is :"+ address);
//                }
//            });
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGpsUtils = new GpsUtils(requireActivity());
        if (checkGooglePlayServices() && isLocationPermissionGranted()) {
            Log.d(">>>>Location", "Oncreate");
            LocationProvider.getInstance().start(requireContext(), mLocationListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.explore_tutor_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = ExploreTutorFragmentBinding.bind(view);
        if (!isLocationPermissionGranted()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 2321);
        }
        getTutors();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisible() && isResumed()) {
            if (!mGpsUtils.isGpsEnabled()) {
                Log.d(">>>>Location", "onResume 1");
                mGpsUtils.turnGPSOn(new GpsUtils.OnGpsListener() {
                    @Override
                    public void onGpsStatus(boolean isGPSEnable) {
                        Log.d(">>>>Location", "OonResume 2");
                    }
                });
            } else {
                Log.d(">>>>Location", "Enabled");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocationProvider.getInstance().stopLocationUpdates();
    }

    private void getTutors() {
        getFirebaseStore().collection(getString(R.string.db_root_tutors)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        users.add(user);
                        Log.d(">>>Explore", "Data Is " + user.firstName + " , " + user.gender);
                    }
                    mBinding.viewPager.setAdapter(new ExplorePagerAdapter(ExploreTutorsFragment.this, users));
                } else {
                    Log.d(">>>Explore", "Error getting documents: ", task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(">>>Explore", "Error getting documents: ", e);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_RECOVER_PLAY_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                LocationProvider.getInstance().start(requireContext(), mLocationListener);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(requireContext(), "Google Play Services must be installed.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2321) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkGooglePlayServices() && isLocationPermissionGranted()) {
                    LocationProvider.getInstance().start(requireContext(), mLocationListener);
                }
            }
        }
    }

    private static class ExplorePagerAdapter extends PagerAdapter {
        private final List<User> mData = new ArrayList<>();
        private LayoutInflater mInflater;
        private ExploreTutorsFragment mTutorsFragment;

        private ExplorePagerAdapter(ExploreTutorsFragment fragment, List<User> data) {
            mData.addAll(data);
            mTutorsFragment = fragment;
            mInflater = LayoutInflater.from(mTutorsFragment.requireActivity());
        }

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = (User) v.getTag();
                Bundle bundle = new Bundle();
                bundle.putParcelable(AppConstants.KEY_DATA, user);
                ActivityUtils.launchFragment(v.getContext(), FullProfileTutorFragment.class.getName(), bundle);
            }
        };

        @Override
        public int getCount() {
            return mData.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ItemExploreTeacherBinding binding = ItemExploreTeacherBinding.inflate(mInflater, container, false);
            User user = mData.get(position);
            binding.setModel(new UserViewModel(user));
            binding.viewMore.setTag(user);
            binding.viewMore.setOnClickListener(mOnClickListener);
            if (!TextUtils.isEmpty(user.pictureUrl)) {
                StorageReference pathReference1 = FirebaseStorage.getInstance().getReference().child(user.pictureUrl);
                GlideApp.with(mInflater.getContext())
                        .load(pathReference1)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fallback(R.drawable.default_pic)
                        .into(binding.profilePicture);
            }
            List<String> subjects = user.getSubjectsToTeachAsArray();
            for (String subject : subjects) {
                ItemSubjectBinding subjectsBinding = ItemSubjectBinding.inflate(mInflater, binding.subjects, false);
                subjectsBinding.setText(subject.trim());
                binding.subjects.addView(subjectsBinding.getRoot());
            }
            container.addView(binding.getRoot());
            Location toLocation = new Location(LocationManager.GPS_PROVIDER);
            toLocation.setLatitude(user.latitude);
            toLocation.setLongitude(user.longitude);
            binding.distance.setText(LocationProvider.getInstance().getDistance(mTutorsFragment.mCurrentLocation, toLocation));
            return binding.getRoot();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
