package com.autohub.skln.student;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.explore_tutor_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = ExploreTutorFragmentBinding.bind(view);
        getTutors();
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
                    mBinding.viewPager.setAdapter(new ExplorePagerAdapter(requireContext(), users));
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

    private static class ExplorePagerAdapter extends PagerAdapter {
        private final List<User> mData = new ArrayList<>();
        private LayoutInflater mInflater;

        private ExplorePagerAdapter(Context context, List<User> data) {
            mData.addAll(data);
            mInflater = LayoutInflater.from(context);
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
            Log.d(">>>Url", user.pictureUrl);
            StorageReference pathReference1 = FirebaseStorage.getInstance().getReference().child(user.pictureUrl);
            GlideApp.with(mInflater.getContext())
                    .load(pathReference1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fallback(R.drawable.default_pic)
                    .into(binding.profilePicture);
            List<String> subjects = user.getSubjectsToTeachAsArray();
            for (String subject : subjects) {
                ItemSubjectBinding subjectsBinding = ItemSubjectBinding.inflate(mInflater, binding.subjects, false);
                subjectsBinding.setText(subject.trim());
                binding.subjects.addView(subjectsBinding.getRoot());
            }
            container.addView(binding.getRoot());
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
