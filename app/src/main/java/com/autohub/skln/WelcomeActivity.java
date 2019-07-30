package com.autohub.skln;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.autohub.skln.databinding.ActivityWelcomeActivityBinding;
import com.autohub.skln.utills.AppConstants;
import com.hbb20.CountryCodePicker;

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

//        setCustomTypeface((TextView) findViewById(R.id.tvWelcome), FONT_TYPE_CERAPRO_BOLD);
//        setCustomTypeface((TextView) findViewById(R.id.etPhoneNumber), FONT_TYPE_CERAPRO_BOLD);
        setCustomTypeface(((CountryCodePicker) findViewById(R.id.codePicker)).getTextView_selectedCountry(), FONT_TYPE_CERAPRO_BOLD);
//        setCustomTypeface((TextView) findViewById(R.id.tvS), FONT_TYPE_MONTSERRAT_BOLD);
//        setCustomTypeface((TextView) findViewById(R.id.tvEnterPhonenum), FONT_TYPE_MONTSERRAT_BOLD);
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
        intent.putExtra(AppConstants.KEY_PHONE_NUMBER, mBinding.codePicker.getFullNumberWithPlus());
        startActivity(intent);
        finish();
    }
}
