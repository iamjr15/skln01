package com.autohub.skln.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.autohub.skln.CropActivity;
import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentEditProfileBinding;
import com.autohub.skln.listeners.DialogFragmentButtonPressedListener;
import com.autohub.skln.listeners.TextWatcherWrapper;
import com.autohub.skln.models.User;
import com.autohub.skln.models.UserViewModel;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
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
import java.util.Locale;
import java.util.Map;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class EditProfileFragment extends BaseFragment implements View.OnClickListener {
    private static final int MAX_SIZE = 240;
    private FragmentEditProfileBinding mBinding;
    private UserViewModel mUserViewModel;
    private StorageReference mStorageReference;
    private final TextWatcherWrapper mWatcherWrapper = new TextWatcherWrapper() {
        @Override
        public void afterTextChanged(Editable s) {
            int remaining = MAX_SIZE - s.length();
            mBinding.count.setText(String.format(Locale.getDefault(), "%d remaining", remaining));
            editBio(s.toString());
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentEditProfileBinding.bind(view);
        mStorageReference = FirebaseStorage.getInstance().getReference();
        mBinding.setCallback(this);
        mUserViewModel = new UserViewModel(new User());
        mBinding.setUserViewModel(mUserViewModel);
        mBinding.classToTeach.setOnClickListener(this);
        mBinding.selectOccupation.setOnClickListener(this);
        mBinding.bio.addTextChangedListener(mWatcherWrapper);
        setupProfile();
        setUpUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.class_to_teach:
                showEditClassesDialog(new DialogFragmentButtonPressedListener<User>() {
                    @Override
                    public void onButtonPressed(User user) {
                        mUserViewModel.setUser(user);
                    }
                });
                break;
            case R.id.select_occupation:
                showDialog(getString(R.string.select_occupation),mUserViewModel.getUser().occupation, AppConstants.KEY_OCCUPATION, getResources().getStringArray(R.array.occupation_arrays), new DialogFragmentButtonPressedListener<String>() {
                    @Override
                    public void onButtonPressed(String s) {
                        User user = mUserViewModel.getUser();
                        user.occupation = s;
                        mUserViewModel.setUser(user);
                    }
                });
                break;
        }
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
        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User user = User.prepareUser(documentSnapshot);
                        mUserViewModel.setUser(user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
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
                        mBinding.profilePicture.setTag(pickResult.getPath());

                        File file = new File(pickResult.getPath());
                        Intent intent = new Intent(requireActivity(), CropActivity.class);
                        intent.putExtra(AppConstants.KEY_URI, Uri.fromFile(file));
                        startActivityForResult(intent, 1122);
                    }
                }
            }).show(requireActivity());
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
            final StorageReference picRef = mStorageReference.child("tutor/" + getFirebaseAuth().getCurrentUser().getUid() + ".jpg");
            UploadTask uploadTask = picRef.putBytes(bytes);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    hideLoading();
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

    private void editBio(final String bio) {
        Map<String, Object> user = new HashMap<>();
        user.put(AppConstants.KEY_BIODATA, bio);

        FirebaseFirestore.getInstance().collection(getString(R.string.db_root_tutors)).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        mUserViewModel.setBioData(bio);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    private void showEditClassesDialog(DialogFragmentButtonPressedListener<User> listener) {
        DialogFragmentButtonPressedListener<User> dialogFragmentButtonPressedListener = new DialogFragmentButtonPressedListener<User>() {
            @Override
            public void onButtonPressed(User user) {
                mUserViewModel.setUser(user);
            }
        };
        EditClassesDialogFragment.newInstance(mUserViewModel.getUser(), listener).show(getFragmentManager(), EditClassesDialogFragment.class.getName());
    }

    private void showDialog(String title, String selectedValue, String key, String[] data, DialogFragmentButtonPressedListener<String> listener) {
        EditDialogFragment.newInstance(title, selectedValue, key, data, listener).show(getFragmentManager(), EditDialogFragment.class.getName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122 && resultCode == Activity.RESULT_OK && data != null) {
            Uri croppedUri = data.getParcelableExtra("_cropped_uri_");
            Uri originalUri = data.getParcelableExtra("_original_uri_");
            Log.d(">>>RegisterAcRes", croppedUri.toString() + " , " + originalUri.toString());
            mBinding.profilePicture.setImageURI(croppedUri);
            mBinding.profilePicture.setTag(croppedUri.getPath());
            uploadImage(croppedUri);
        }
    }
}
