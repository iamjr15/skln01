package com.autohub.skln.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentTutorHomeBinding;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;

public class FragmentHome extends BaseFragment {
    private FragmentTutorHomeBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutor_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentTutorHomeBinding.bind(view);
        setupProfile();
    }

    private void setupProfile() {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("tutor/" +
                getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
        GlideApp.with(this)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding.ivPicture);

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String firstName = documentSnapshot.getString(KEY_FIRST_NAME);
                        mBinding.heyUser.setText("Hey, \n" + firstName + ".");
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
