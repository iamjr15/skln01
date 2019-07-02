package com.autohub.skln;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity {
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    @BindView(R.id.codePicker)
    CountryCodePicker codePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_activity);
        ButterKnife.bind(this);

        ImageButton btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, NumberVerificationActivity.class);
                intent.putExtra("country_code", "+" + codePicker.getSelectedCountryCode());
                intent.putExtra("phone_number", etPhoneNumber.getText().toString());
                startActivity(intent);
                finish();
            }
        });

        setCustomTypeface((TextView) findViewById(R.id.tvWelcome), FONT_TYPE_CERAPRO_BOLD);
        setCustomTypeface((TextView) findViewById(R.id.etPhoneNumber), FONT_TYPE_CERAPRO_BOLD);
        setCustomTypeface(((CountryCodePicker)findViewById(R.id.codePicker)).getTextView_selectedCountry(), FONT_TYPE_CERAPRO_BOLD);
        setCustomTypeface((TextView) findViewById(R.id.tvS), FONT_TYPE_MONTSERRAT_BOLD);
        setCustomTypeface((TextView) findViewById(R.id.tvEnterPhonenum), FONT_TYPE_MONTSERRAT_BOLD);
    }
}
