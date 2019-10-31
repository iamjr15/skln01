package com.autohub.skln.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.autohub.skln.R;
import com.autohub.skln.databinding.DialogFragmentChooserBinding;
import com.autohub.skln.databinding.ItemRadioButtonBinding;
import com.autohub.skln.listeners.DialogFragmentButtonPressedListener;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class ChooserDialogFragment extends BaseDialogFragment {
    private DialogFragmentButtonPressedListener<String> mListener;
    private final CompoundButton.OnCheckedChangeListener mCheckChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String data = (String) buttonView.getTag();
            if (isChecked) {
                dismiss();
                mListener.onButtonPressed(data);
            }
        }
    };

    public static ChooserDialogFragment newInstance(@NonNull String title, @NonNull String[] data, @NonNull DialogFragmentButtonPressedListener<String> dialogFragmentButtonPressedListener) {
        ChooserDialogFragment editDialogFragment = new ChooserDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("_listener_", dialogFragmentButtonPressedListener);
        bundle.putStringArray("_data_", data);
        bundle.putString("_title_", title);
        editDialogFragment.setArguments(bundle);
        return editDialogFragment;
    }

    private String mTitle;
    private String[] mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mListener = bundle.getParcelable("_listener_");
        mData = bundle.getStringArray("_data_");
        mTitle = bundle.getString("_title_");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(requireContext());
        final DialogFragmentChooserBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_fragment_chooser, null, false);
        binding.heading.setText(mTitle);
        for (String data : mData) {
            ItemRadioButtonBinding b = DataBindingUtil.inflate(layoutInflater, R.layout.item_radio_button, binding.radioContainer, false);
            b.radioButton.setTag(data);
            b.radioButton.setOnCheckedChangeListener(mCheckChangeListener);
            b.title.setText(String.format("%s", data));
            binding.radioContainer.addView(b.getRoot());
        }

        return new AlertDialog.Builder(requireActivity())
                .setView(binding.getRoot()).create();
    }
}
