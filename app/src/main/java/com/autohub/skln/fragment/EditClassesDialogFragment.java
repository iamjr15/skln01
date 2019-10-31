package com.autohub.skln.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.autohub.skln.R;
import com.autohub.skln.databinding.DialogFragmentEditClassesBinding;
import com.autohub.skln.databinding.ItemClassCheckboxBinding;
import com.autohub.skln.listeners.DialogFragmentButtonPressedListener;
import com.autohub.skln.models.User;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.CommonUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class EditClassesDialogFragment extends BaseDialogFragment {
    private List<ItemClassCheckboxBinding> mCheckboxBindings = new ArrayList<>();
    private DialogFragmentButtonPressedListener<User> mListener;
    private HashSet<String> mSelectedClasses = new HashSet<>();
    private final CompoundButton.OnCheckedChangeListener mCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String clazz = String.valueOf((int) buttonView.getTag());
            if (isChecked) {
                mSelectedClasses.add(clazz);
            } else {
                mSelectedClasses.remove(clazz);
            }
        }
    };

    public static EditClassesDialogFragment newInstance(User user, DialogFragmentButtonPressedListener<User> dialogFragmentButtonPressedListener) {
        EditClassesDialogFragment editDialogFragment = new EditClassesDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("_listener_", dialogFragmentButtonPressedListener);
        bundle.putParcelable("_user_", user);
        editDialogFragment.setArguments(bundle);
        return editDialogFragment;
    }

    private User mUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListener = bundle.getParcelable("_listener_");
        mUser = bundle.getParcelable("_user_");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        DialogFragmentEditClassesBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_fragment_edit_classes, null, false);
        mCheckboxBindings.add(binding.class1);
        mCheckboxBindings.add(binding.class2);
        mCheckboxBindings.add(binding.class3);
        mCheckboxBindings.add(binding.class4);
        mCheckboxBindings.add(binding.class5);
        mCheckboxBindings.add(binding.class6);
        mCheckboxBindings.add(binding.class7);
        mCheckboxBindings.add(binding.class8);
        mCheckboxBindings.add(binding.class9);
        mCheckboxBindings.add(binding.class10);
        mCheckboxBindings.add(binding.class11);
        mCheckboxBindings.add(binding.class12);
        mSelectedClasses = mUser.getClasses();
        int i = 1;
        for (ItemClassCheckboxBinding b : mCheckboxBindings) {
            b.cb.setTag(i);
            b.cb.setChecked(mSelectedClasses.contains(String.valueOf(i)));
            b.cb.setOnCheckedChangeListener(mCheckChangeListener);
            b.title.setText(String.format("Class %s", i++));
        }
        final AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setView(binding.getRoot()).create();
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedClasses.size() == 0) {
                    showSnackError(R.string.select_class_text);
                    return;
                }
                updateData();
            }
        });
        return alertDialog;
    }

    private void updateData() {
        if ((mSelectedClasses.contains(AppConstants.CLASS_11) || mSelectedClasses.contains(AppConstants.CLASS_12)) && CommonUtils.hasLowerClasses(mSelectedClasses, new CommonUtils.Function<HashSet<String>>() {
            @Override
            public boolean apply(HashSet<String> data) {
                for (int i = 1; i <= 10; i++) {
                    if (data.contains(String.valueOf(i))) {
                        return true;
                    }
                }
                return false;
            }
        })) {
            showSnackError(R.string.you_can_select_class_11_12_or_vice_versa);
            return;
        }
        showLoading();
        final Map<String, Object> map = new HashMap<>();
        final String classes = CommonUtils.getClasses(mSelectedClasses);
        map.put(AppConstants.KEY_CLASSES, classes);

        FirebaseFirestore.getInstance().collection(getString(R.string.db_root_tutors)).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        dismiss();
                        mUser.classesToTeach = classes;
                        mListener.onButtonPressed(mUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                    }
                });
    }
}
