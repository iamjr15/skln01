package com.autohub.skln.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.databinding.FragmentRequestDetailBinding;
import com.autohub.skln.models.Request;
import com.autohub.skln.models.RequestViewModel;
import com.autohub.skln.models.User;
import com.autohub.skln.models.UserViewModel;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-05.
 */
public class FragmentRequestDetail extends BaseFragment {
    private RequestViewModel mRequestViewModel;
    private FragmentRequestDetailBinding mBinding;
    private User mStudent, mTutor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) requireActivity()).setStatusBarColor(R.drawable.purple_header);
        mRequestViewModel = getArguments().getParcelable(AppConstants.KEY_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = FragmentRequestDetailBinding.bind(view);
        mBinding.setRequestViewModel(mRequestViewModel);
        mBinding.deleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = Request.STATUS.DELETED.getValue();
                if (mRequestViewModel.getUserType().equalsIgnoreCase("student")) {
                    status = Request.STATUS.CANCELED.getValue();
                }
                changeStatus(status);
            }
        });
        fetchStudent();
        fetchTutor();

        setUpUi();
        mBinding.contactStd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opDialer();
            }
        });
    }

    private void setUpUi() {
        if (mRequestViewModel.getUserType().equalsIgnoreCase("tutor")) {
            mBinding.deleteRequest.setText(R.string.delete_request);
            mBinding.deleteRequest.setEnabled(!mRequestViewModel.getRequest().requestStatus.equalsIgnoreCase(Request.STATUS.DELETED.getValue()));
            mBinding.messageLabel.setText(R.string.request_message);
            mBinding.requestLogo.setImageResource(R.drawable.ic_flash);
            mBinding.requestStatus.setText(R.string.new_request_received);
            mBinding.contactStd.setText(R.string.contact_student);
            mBinding.addBatch.setText(R.string.add_to_batch);
        } else {
            mBinding.deleteRequest.setEnabled(!mRequestViewModel.getRequest().requestStatus.equalsIgnoreCase(Request.STATUS.CANCELED.getValue()));
            mBinding.deleteRequest.setText(R.string.cancel_request);
            mBinding.messageLabel.setText(R.string.request_message_student);
            mBinding.requestLogo.setImageResource(R.drawable.ic_request_tick);
            mBinding.requestStatus.setText(R.string.new_request_sent);
            mBinding.contactStd.setText(R.string.contact_skill_master);
            mBinding.addBatch.setText(R.string.see_location_on_map);
        }
    }

    private void loadPicture() {
        if (mStudent == null || mTutor == null) return;
        String path = mTutor.pictureUrl;
        if (mRequestViewModel.getUserType().equalsIgnoreCase("tutor")) {
            path = mStudent.pictureUrl;
        }
        StorageReference pathReference1 = FirebaseStorage.getInstance().getReference().child(path);
        GlideApp.with(requireContext())
                .load(pathReference1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fallback(R.drawable.default_pic)
                .into(mBinding.profilePicture);
    }

    private void fetchStudent() {
        String root = getString(R.string.db_root_students);
        getFirebaseStore().collection(root).document(mRequestViewModel.getRequest().studentId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        User student = documentSnapshot.toObject(User.class);
                        if (mRequestViewModel.getUserType().equalsIgnoreCase("tutor")) {
                            mBinding.setUserViewModel(new UserViewModel(student));
                        }
                        mStudent = student;
                        loadPicture();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });
    }

    private void fetchTutor() {
        String root = getString(R.string.db_root_tutors);
        getFirebaseStore().collection(root).document(mRequestViewModel.getRequest().tutorId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        hideLoading();
                        User tutor = documentSnapshot.toObject(User.class);
                        if (mRequestViewModel.getUserType().equalsIgnoreCase("student")) {
                            mBinding.setUserViewModel(new UserViewModel(tutor));
                        }
                        mBinding.setTutorViewModel(new UserViewModel(tutor));
                        mTutor = tutor;
                        loadPicture();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        showSnackError(e.getMessage());
                    }
                });
    }

    private void changeStatus(final String requestStatus) {
        showLoading();
        HashMap<String, String> map = new HashMap<>();
        map.put("requestStatus", requestStatus);
        String dbRoot = getString(R.string.db_root_requests);
        getFirebaseStore().collection(dbRoot).document(mRequestViewModel.getRequestId()).set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideLoading();
                mRequestViewModel.getRequest().requestStatus = requestStatus;
                setUpUi();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideLoading();
            }
        });
    }

    private void opDialer() {
        String number = "tel:" + mStudent.phoneNumber;
        if (mRequestViewModel.getUserType().equalsIgnoreCase("student")) {
            number = "tel:" + mTutor.phoneNumber;
        }
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(number));
        startActivity(intent);
    }
}
