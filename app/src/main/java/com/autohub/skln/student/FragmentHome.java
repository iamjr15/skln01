package com.autohub.skln.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentStudentHomeBinding;
import com.autohub.skln.databinding.ItemSubjectsBinding;
import com.autohub.skln.fragment.BaseFragment;
import com.autohub.skln.models.User;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FragmentHome extends BaseFragment {
    private FragmentStudentHomeBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_student_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentStudentHomeBinding.bind(view);
//        setupProfile();
        setUpUserInfo();
    }

    private void setupProfile() {
        StorageReference ref = FirebaseStorage.getInstance().getReference().child("tutor/" +
                getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
        GlideApp.with(this)
                .load(ref)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding.profilePicture);
    }

    private void setUpUserInfo() {
        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = documentSnapshot.toObject(User.class);
                        user.id = documentSnapshot.getId();
//                        User user = User.prepareUser(documentSnapshot);
                        mBinding.heyUser.setText(String.format("Hey, \n%s.", user.firstName));
                        setSubjects(user);
                        setHobbies(user);
//                        mUserViewModel.setUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
    }

    private void setSubjects(User user) {
        for (Integer integer : user.getFavoriteClasses()) {
            ItemSubjectsBinding binding = ItemSubjectsBinding.inflate(LayoutInflater.from(requireContext()), mBinding.containerAcademic, false);
            binding.image.setImageResource(integer);
            mBinding.containerAcademic.addView(binding.getRoot());
        }
    }

    private void setHobbies(User user) {
        for (Integer integer : user.getHobbies()) {
            ItemSubjectsBinding binding = ItemSubjectsBinding.inflate(LayoutInflater.from(requireContext()), mBinding.containerHobbies, false);
            binding.image.setImageResource(integer);
            mBinding.containerHobbies.addView(binding.getRoot());
        }
    }
}
