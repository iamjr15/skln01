package com.autohub.skln.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.autohub.skln.CropActivity;
import com.autohub.skln.R;
import com.autohub.skln.activities.user.WelcomeActivity;
import com.autohub.skln.databinding.FragmentTutorProfileBinding;
import com.autohub.skln.tutor.EditProfileFragment;
import com.autohub.skln.utills.ActivityUtils;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;

public class FragmentProfile extends BaseFragment {
    private FragmentTutorProfileBinding mBinding;
    private String mProfileType = "tutor";
    private StorageReference mStorageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tutor_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mProfileType = getArguments().getString(AppConstants.KEY_TYPE, "tutor");
        }
        mBinding = FragmentTutorProfileBinding.bind(view);
        mBinding.setCalback(this);
        mStorageReference = FirebaseStorage.getInstance().getReference();
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
                FirebaseAuth.getInstance().signOut();
                ActivityUtils.launchActivity(requireContext(), WelcomeActivity.class);
                requireActivity().finishAffinity();
            }
        });
        mBinding.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddPicture();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //setupProfile();
    }

    private void setupProfile() {
        String path = "tutor/";
        if (mProfileType.equalsIgnoreCase("student")) {
            path = "student/";
        }
        StorageReference ref = FirebaseStorage.getInstance().getReference().child(path +
                getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
        GlideApp.with(this)
                .load(ref)
                .placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.NONE)  // disable caching of glide
                .skipMemoryCache(true)
                .into(mBinding.profilePicture);

        String root = getString(R.string.db_root_tutors);
        if (mProfileType.equalsIgnoreCase("student")) {
            root = getString(R.string.db_root_students);
        }
        getFirebaseStore().collection(root).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String firstName = documentSnapshot.getString(KEY_FIRST_NAME);
                        String lastName = documentSnapshot.getString(KEY_LAST_NAME);
                        if (!TextUtils.isEmpty(lastName)) {
                            mBinding.name.setText(String.format("%s %s.", firstName,
                                    lastName.substring(0, 1)));
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

    public void onEditProfileClick() {
        if (mProfileType.equalsIgnoreCase("student")) {
            ActivityUtils.launchFragment(requireContext(), com.autohub.skln.student.EditProfileFragment.class.getName());
            return;
        }
        ActivityUtils.launchFragment(requireContext(), EditProfileFragment.class.getName());
    }

    public void onEditPersonalDetailClick() {

    }

    public void onManagePackagesClick() {

    }

    public void onAddPicture() {
        String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!checkIfAlreadyhavePermission()) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return;
            }
            ActivityCompat.requestPermissions(requireActivity(), galleryPermissions, 0);
        } else {
            PickSetup setup = new PickSetup();
            setup.setCancelText("Close");
            PickImageDialog.build(setup, new IPickResult() {
                @Override
                public void onPickResult(PickResult pickResult) {
                    if (pickResult.getError() == null) {
                        Uri uri = pickResult.getUri();
                        mBinding.profilePicture.setImageURI(uri);
                        File file = new File(pickResult.getPath());
                        Intent intent = new Intent(requireActivity(), CropActivity.class);
                        intent.putExtra(AppConstants.KEY_URI, Uri.fromFile(file));
                        startActivityForResult(intent, 1122);
                    }
                }
            }).show(requireActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122 && resultCode == Activity.RESULT_OK && data != null) {
            Uri croppedUri = data.getParcelableExtra("_cropped_uri_");
            mBinding.profilePicture.setImageURI(croppedUri);
            mBinding.profilePicture.setTag(croppedUri.getPath());
            uploadImage(croppedUri);
        }
    }

    private void uploadImage(Uri uri) {
        showLoading();
        File file = new File(uri.getPath());
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();

            String path = "tutor/";
            if (mProfileType.equalsIgnoreCase("student")) {
                path = "student/";
            }
            final String pathString = path + getFirebaseAuth().getCurrentUser().getUid() + ".jpg";
            final StorageReference picRef = FirebaseStorage.getInstance().getReference().child(pathString);
            UploadTask uploadTask = picRef.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideLoading();
                    saveUserPhoneOnFirestore(pathString);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideLoading();
                    Toast.makeText(requireContext(), "Upload Failed -> " + e, Toast.LENGTH_LONG).show();
                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            hideLoading();
        } catch (IOException e) {
            e.printStackTrace();
            hideLoading();
        }
    }

    private void saveUserPhoneOnFirestore(String pathString) {
        Map<String, Object> user = new HashMap<>();
        user.put(AppConstants.KEY_PROFILE_PICTURE, pathString);

        String root = getString(R.string.db_root_tutors);
        if (mProfileType.equalsIgnoreCase("student")) {
            root = getString(R.string.db_root_students);
        }

       getFirebaseStore().collection(root).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

}
