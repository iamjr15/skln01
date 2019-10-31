package com.autohub.skln.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.databinding.ActivityWelcomeActivityBinding;
import com.autohub.skln.utills.ActivityUtils;
import com.autohub.skln.utills.AppConstants;
import com.hbb20.CountryCodePicker;

import static com.autohub.skln.utills.AppConstants.KEY_USERMAP;

public class WelcomeActivity extends BaseActivity implements TextView.OnEditorActionListener {
    private ActivityWelcomeActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome_activity);
        mBinding.codePicker.registerCarrierNumberEditText(mBinding.etPhoneNumber);
        mBinding.etPhoneNumber.setOnEditorActionListener(this);
        ImageButton btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.codePicker.isValidFullNumber()) {
                    moveNext();
                } else {
                    Toast.makeText(WelcomeActivity.this, R.string.enter_valid_number, Toast.LENGTH_SHORT).show();
                }
            }
        });
        setCustomTypeface(((CountryCodePicker) findViewById(R.id.codePicker)).getTextView_selectedCountry(), FONT_TYPE_CERAPRO_BOLD);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE && mBinding.codePicker.isValidFullNumber()) {
            moveNext();
            return true;
        }
        Toast.makeText(WelcomeActivity.this, R.string.enter_valid_number, Toast.LENGTH_SHORT).show();
        return false;
    }

    private void moveNext() {
        Intent intent = new Intent(WelcomeActivity.this, NumberVerificationActivity.class);
        Bundle bundle = this.getIntent().getExtras();

        bundle.putString(AppConstants.KEY_PHONE_NUMBER, mBinding.codePicker.getFullNumberWithPlus());

        if (bundle != null) {
            bundle.putSerializable(KEY_USERMAP, bundle.getSerializable(KEY_USERMAP));

        }

        ActivityUtils.launchActivity(this, NumberVerificationActivity.class, bundle);

        //  finish();
    }
}
