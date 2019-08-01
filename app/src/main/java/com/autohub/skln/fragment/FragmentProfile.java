package com.autohub.skln.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autohub.skln.R;
import com.autohub.skln.WelcomeActivity;
import com.autohub.skln.databinding.FragmentTutorProfileBinding;
import com.autohub.skln.utills.ActivityUtils;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;

public class FragmentProfile extends BaseFragment {
    private FragmentTutorProfileBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentTutorProfileBinding.bind(view);
        AppCompatActivity appCompatActivity = (AppCompatActivity) requireActivity();
        appCompatActivity.setSupportActionBar(mBinding.toolBar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mBinding.collapsingToolbar.setContentScrim(ContextCompat.getDrawable(requireContext(), R.drawable.tutor_profile_header));
        mBinding.appbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int actualHeight, actualWidth, totalScroll = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (totalScroll == -1) {
                    totalScroll = appBarLayout.getTotalScrollRange();
                }
                int currentScroll = totalScroll + verticalOffset;
                ViewGroup.LayoutParams picParams = mBinding.profilePicture.getLayoutParams();
                float ratio = (float) currentScroll / totalScroll;
                if (actualHeight == 0) {
                    actualHeight = mBinding.profilePicture.getMeasuredHeight();
                    actualWidth = mBinding.profilePicture.getMeasuredWidth();
                }
                picParams.height = (int) (ratio * actualHeight);
                picParams.height = (int) (ratio * actualWidth);
                mBinding.profilePicture.setLayoutParams(picParams);

                mBinding.name.setAlpha(ratio);
                int i = totalScroll + verticalOffset;
                if (i == 0) {
                    mBinding.labelProfile.setText(mBinding.name.getText());
                } else {
                    mBinding.labelProfile.setText(R.string.profile);
                }
            }
        });
        setupProfile();
        mBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityUtils.launchActivity(requireContext(), WelcomeActivity.class);
                requireActivity().finishAffinity();
            }
        });
    }

    private void setupProfile() {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("tutor/" +
                getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
        GlideApp.with(this)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding.profilePicture);

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String firstName = documentSnapshot.getString(KEY_FIRST_NAME);
                        String lastName = documentSnapshot.getString(KEY_LAST_NAME);
                        if (!TextUtils.isEmpty(lastName)) {
                            mBinding.name.setText(String.format("%s %s.", firstName, lastName.substring(0, 1)));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
    }
}
