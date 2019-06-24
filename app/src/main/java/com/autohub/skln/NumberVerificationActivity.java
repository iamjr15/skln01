package com.autohub.skln;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.autohub.skln.tutor.activity.TutorCategorySelect;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class NumberVerificationActivity extends BaseActivity {
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;

    @BindView(R.id.codePicker)
    CountryCodePicker codePicker;

    @BindView(R.id.pinView)
    Pinview pinView;

    @BindView(R.id.btnNext)
    Button btnNext;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mFirebaseAuth;

    private String verificationId;
    private String countryCode;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verification);
        ButterKnife.bind(this);

        countryCode = getIntent().getExtras().getString("country_code");
        phoneNum = getIntent().getExtras().getString("phone_number");

        codePicker.setFullNumber(countryCode);
        codePicker.setEnabled(false);
        tvPhoneNumber.setText(phoneNum);

        mFirebaseAuth = FirebaseAuth.getInstance();

        setCallback();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countryCode + phoneNum,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    // PhoneAuthProvider callback
    private void setCallback() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                } else if (e instanceof FirebaseTooManyRequestsException) {

                }
            }

            @Override
            public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken token) {
                verificationId = id;
            }
        };

    }
    /*The below method is for to validate the user the contact number is either valid or not*/

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        /*The below method is for to validate the user from the client*/

        if (pinView.getValue().length() != pinView.getPinLength()) {
            Snackbar snackbar;
            snackbar = Snackbar.make((findViewById(android.R.id.content)), getString(R.string.enter_correct_otp), Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.parseColor("#ba0505"));
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return;
        }
        showLoading();
        /*The below Code is for to validate the user from the server side*/
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, pinView.getValue());
        signInWithPhoneAuthCredential(credential);
    }

    /*This is resend function is for sending the sms to the Contact number */
    @OnClick(R.id.btnResendCode)
    public void onResendClick() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                countryCode + phoneNum,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
        Toast.makeText(this, R.string.sending, Toast.LENGTH_SHORT).show();
    }

    // Verifying OTP
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideLoading();
                            getAppPreferenceHelper().setTutorId(task.getResult().getUser().getUid());
                            getAppPreferenceHelper().setTutorPhoneNumber(countryCode + phoneNum);
                            startActivity(new Intent(NumberVerificationActivity.this, TutorCategorySelect.class));
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                hideLoading();
                            }
                        }
                    }
                });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
