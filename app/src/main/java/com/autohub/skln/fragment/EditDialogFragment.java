package com.autohub.skln.fragment;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;

import com.autohub.skln.R;
import com.autohub.skln.databinding.DialogFragmentEditBinding;
import com.autohub.skln.databinding.ItemRadioButtonBinding;
import com.autohub.skln.listeners.DialogFragmentButtonPressedListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class EditDialogFragment extends BaseDialogFragment {
    private DialogFragmentButtonPressedListener<String> mListener;
    private final CompoundButton.OnCheckedChangeListener mCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String data = (String) buttonView.getTag();
            if (isChecked) {
                mNewSelection = data;
            }
        }
    };

    public static EditDialogFragment newInstance(@NonNull String title, @NonNull String selectedValue, @NonNull String key, @NonNull String[] data, @NonNull DialogFragmentButtonPressedListener<String> dialogFragmentButtonPressedListener) {
        EditDialogFragment editDialogFragment = new EditDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("_listener_", dialogFragmentButtonPressedListener);
        bundle.putStringArray("_data_", data);
        bundle.putString("_key_", key);
        bundle.putString("_selected_value_", selectedValue);
        bundle.putString("_title_", title);
        editDialogFragment.setArguments(bundle);
        return editDialogFragment;
    }

    private String mTitle;
    private String mKey;
    private String[] mData;
    private String mSelectedValue, mNewSelection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListener = bundle.getParcelable("_listener_");
        mData = bundle.getStringArray("_data_");
        mKey = bundle.getString("_key_");
        mTitle = bundle.getString("_title_");
        mNewSelection = mSelectedValue = bundle.getString("_selected_value_");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        final DialogFragmentEditBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_fragment_edit, null, false);
        binding.heading.setText(mTitle);
        for (String data : mData) {
            ItemRadioButtonBinding b = DataBindingUtil.inflate(layoutInflater, R.layout.item_radio_button, binding.radioContainer, false);
            b.radioButton.setTag(data);
            b.radioButton.setChecked(mSelectedValue.equalsIgnoreCase(data));
            b.radioButton.setOnCheckedChangeListener(mCheckChangeListener);
            b.title.setText(String.format("%s", data));
            binding.radioContainer.addView(b.getRoot());
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                .setView(binding.getRoot()).create();
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectedValue.equalsIgnoreCase(mNewSelection)) {
                    showSnackError(R.string.select_different_value);
                    return;
                }
                updateData(mNewSelection);
            }
        });
        return alertDialog;
    }

    private void updateData(final String value) {
        showLoading();
        final Map<String, Object> map = new HashMap<>();
        map.put(mKey, value);

        FirebaseFirestore.getInstance().collection(getString(R.string.db_root_tutors)).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        dismiss();
                        mListener.onButtonPressed(value);
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
