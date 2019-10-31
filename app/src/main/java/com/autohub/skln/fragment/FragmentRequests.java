package com.autohub.skln.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.autohub.skln.FrameActivity;
import com.autohub.skln.R;
import com.autohub.skln.adapters.RequestAdapter;
import com.autohub.skln.databinding.FragmentRequestsBinding;
import com.autohub.skln.listeners.ItemClickListener;
import com.autohub.skln.models.Request;
import com.autohub.skln.models.RequestViewModel;
import com.autohub.skln.models.User;
import com.autohub.skln.utills.AppConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-05.
 */
public class FragmentRequests extends BaseFragment {
    private String mType;
    private String mUserType;
    private User mUser;
    private RequestAdapter mAdapter;
    private final ItemClickListener<RequestViewModel> mItemClickListener = new ItemClickListener<RequestViewModel>() {
        @Override
        public void onClick(RequestViewModel requestViewModel) {
            Intent intent = new Intent(requireContext(), FrameActivity.class);
            intent.putExtra(AppConstants.KEY_FRAGMENT, FragmentRequestDetail.class.getName());
            intent.putExtra(AppConstants.KEY_THEME, R.style.AppTheme_NoActionBar_FrameActivity_Purple);
            Bundle bundle=new Bundle();
            bundle.putParcelable(AppConstants.KEY_DATA,requestViewModel);
            intent.putExtra(AppConstants.KEY_DATA,bundle);
            startActivity(intent);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserType = getArguments().getString("_user_type", "Student");
        mType = getArguments().getString(AppConstants.KEY_TYPE, "Latest");
        mUser = getArguments().getParcelable(AppConstants.KEY_DATA);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentRequestsBinding binding = FragmentRequestsBinding.bind(view);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setEmptyView(binding.noData);
        mAdapter = new RequestAdapter(requireContext(), mItemClickListener);
        binding.recyclerView.setAdapter(mAdapter);
        getRequests();
    }

    private void getRequests() {
        if (mUser == null || mUser.id == null) {
            Log.e(">>>>Nulll", (mUser == null)+"");
            return;
        }
        String dbRoot = getString(R.string.db_root_requests);
        Query query = getFirebaseStore().collection(dbRoot).whereEqualTo("studentId", mUser.id);
        if (mUserType.equalsIgnoreCase("Tutor")) {
            query = getFirebaseStore().collection(dbRoot).whereEqualTo("tutorId", mUser.id);
        }
        if(mType.equalsIgnoreCase("Latest")){
            query= query.whereEqualTo("requestStatus",Request.STATUS.PENDING.getValue());
        }
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<RequestViewModel> requests = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Request request = document.toObject(Request.class);
                        requests.add(new RequestViewModel(request, mUserType,document.getId()));
                        Log.d(">>>Explore", "Data Is " + request.subject);
                    }
                    mAdapter.setData(requests);
                } else {
                    Log.d(">>>Explore", "Error getting documents: ", task.getException());
                }
                hideLoading();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideLoading();
                Log.e(">>>Explore", "Error getting documents: ", e);
            }
        });
    }
}
