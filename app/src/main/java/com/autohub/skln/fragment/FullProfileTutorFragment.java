package com.autohub.skln.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentFullProfileTutorBinding;
import com.autohub.skln.listeners.DialogFragmentButtonPressedListener;
import com.autohub.skln.models.Request;
import com.autohub.skln.models.User;
import com.autohub.skln.models.UserViewModel;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class FullProfileTutorFragment extends BaseFragment {
    private UserViewModel mUserViewModel;
    private FragmentFullProfileTutorBinding mBinding;
    private User mCurrentUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() == null) {
            throw new IllegalArgumentException("Arguments can not be null");
        }
        User user = getArguments().getParcelable(AppConstants.KEY_DATA);
        mUserViewModel = new UserViewModel(user);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_full_profile_tutor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentFullProfileTutorBinding.bind(view);
        mBinding.setModel(mUserViewModel);

        setUpView();
        getCurrentUser();
    }

    private void getCurrentUser() {
        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        mCurrentUser = documentSnapshot.toObject(User.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
    }

    private void setUpView() {
        if(mUserViewModel.getUser().pictureUrl !=null)
        {
            StorageReference pathReference1 = FirebaseStorage.getInstance().getReference().child(mUserViewModel.getUser().pictureUrl);
            GlideApp.with(requireContext())
                    .load(pathReference1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fallback(R.drawable.default_pic)
                    .into(mBinding.profilePicture);
        }


        mBinding.requestThisClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooserDialogFragment.newInstance("Choose Subject", mUserViewModel.getSubjectsToTeachAsArray(), new DialogFragmentButtonPressedListener<String>() {
                    @Override
                    public void onButtonPressed(String values) {
                        makeRequest(values);
                    }
                }).show(getChildFragmentManager(), "Choose Subject");

            }
        });
    }

    private void makeRequest(String subject) {
        showLoading();
        String studentId = getFirebaseAuth().getCurrentUser().getUid();
        String tutorId = mUserViewModel.getUserId();
        Request request = new Request(studentId, tutorId, subject, mUserViewModel.getFirstName(), mCurrentUser.firstName,mCurrentUser.studentClass,mUserViewModel.getClassType());
        String dbRoot = getString(R.string.db_root_requests);
        getFirebaseStore().collection(dbRoot).add(request).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(">>>>Request", "DocumentSnapshot added with ID: " + documentReference.getId());
                hideLoading();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideLoading();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_favorite, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
