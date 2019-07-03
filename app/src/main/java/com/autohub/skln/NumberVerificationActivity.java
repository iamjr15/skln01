package com.autohub.skln;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.autohub.skln.tutor.activity.TutorLogin;
import com.autohub.skln.tutor.activity.TutorOrStudent;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_ACCOUNT_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.TYPE_TUTOR;


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
        codePicker.setClickable(false);
        codePicker.setFocusable(false);
        codePicker.setEnabled(false);
        tvPhoneNumber.setText(phoneNum);

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
            }

            @Override
            public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken token) {
                Toast.makeText(NumberVerificationActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
                verificationId = id;
            }
        };
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        if (pinView.getValue().length() != pinView.getPinLength()) {
            showSnackError(R.string.enter_correct_otp);
            return;
        }

        showLoading();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, pinView.getValue());
        signInWithPhoneAuthCredential(credential);
    }

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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getFirebaseStore().collection(getString(R.string.db_root_users)).document(getFirebaseAuth().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    hideLoading();
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot snapshot = task.getResult();
                                        String phoneNumber = snapshot.getString(KEY_PHONE_NUMBER);
                                        String accountType = snapshot.getString(KEY_ACCOUNT_TYPE);

                                        if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.equals(countryCode + phoneNum) &&
                                        !TextUtils.isEmpty(accountType) && accountType.equals(TYPE_TUTOR)) {
                                            startActivity(new Intent(NumberVerificationActivity.this, TutorLogin.class));
                                            finish();
                                            return;
                                        }
                                    }

                                    getAppPreferenceHelper().setUserPhoneNumber(countryCode + phoneNum);
                                    startActivity(new Intent(NumberVerificationActivity.this, TutorOrStudent.class));
                                    finish();
                                }
                            });
                        } else {
                            if (task.getException() instanceof FirebaseAuthException) {
                                showSnackError(task.getException().getMessage());
                            } else {
                                showSnackError(getString(R.string.error));
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
