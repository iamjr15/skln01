package com.autohub.skln.activities.user;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.BuildConfig;
import com.autohub.skln.R;
import com.autohub.skln.databinding.ActivityNumberVerificationBinding;
import com.autohub.skln.utills.ActivityUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.SetOptions;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_EMAIL;
import static com.autohub.skln.utills.AppConstants.KEY_PASSWORD;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.KEY_USERMAP;


public class NumberVerificationActivity extends BaseActivity implements TextView.OnEditorActionListener {
    private ActivityNumberVerificationBinding mBinding;
    private String mVerificationId;
    private String phoneNum;
    private HashMap<String, Object> userMap;
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace();
            }
            Toast.makeText(NumberVerificationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken token) {
            Toast.makeText(NumberVerificationActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
            mVerificationId = id;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_number_verification);
        mBinding.setCallback(this);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            throw new IllegalArgumentException("Data sent is null");
        }
        userMap = (HashMap<String, Object>) extras.getSerializable(KEY_USERMAP);
//
        mBinding.etPhoneNumber.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mBinding.etPhoneNumber, InputMethodManager.SHOW_IMPLICIT);
        mBinding.codePicker.registerCarrierNumberEditText(mBinding.etPhoneNumber);
        mBinding.etPhoneNumber.setOnEditorActionListener(this);
        mBinding.btngetotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.codePicker.isValidFullNumber()) {
                    phoneNum = mBinding.codePicker.getFullNumberWithPlus();
                    mBinding.pinView.setVisibility(View.VISIBLE);
                    mBinding.btnResendCode.setVisibility(View.VISIBLE);
                    mBinding.btnNext.setVisibility(View.VISIBLE);
                    verifyPhoneNumber();
                } else {
                    Toast.makeText(NumberVerificationActivity.this, R.string.enter_valid_number, Toast.LENGTH_SHORT).show();
                }
            }
        });
        setCustomTypeface(((CountryCodePicker) findViewById(R.id.codePicker)).getTextView_selectedCountry(), FONT_TYPE_CERAPRO_BOLD);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void verifyPhoneNumber() {
        if (mBinding.codePicker.isValidFullNumber()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNum,
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);
            Toast.makeText(this, R.string.sending, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(NumberVerificationActivity.this, R.string.enter_valid_number, Toast.LENGTH_SHORT).show();
        }

    }

    public void onResendClick() {
        if (mBinding.codePicker.isValidFullNumber()) {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mBinding.codePicker.getFullNumberWithPlus(),
                    60,
                    TimeUnit.SECONDS,
                    this,
                    mCallbacks);
            Toast.makeText(this, R.string.sending, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(NumberVerificationActivity.this, R.string.enter_valid_number, Toast.LENGTH_SHORT).show();
        }
    }


    public void onNextClick() {

        if (mVerificationId == null || mBinding.pinView.getValue().length() != mBinding.pinView.getPinLength()) {
            showSnackError(R.string.enter_correct_otp);
            return;
        }
        showLoading();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, mBinding.pinView.getValue());

        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        getFirebaseAuth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideLoading();
                            userMap.put(KEY_PHONE_NUMBER, mBinding.codePicker.getFullNumberWithPlus());
                            showLoading();
                            linkWithCredentials();

                        } else {
                            hideLoading();
                            if (task.getException() instanceof FirebaseAuthException) {
                                showSnackError(task.getException().getMessage());
                            } else {
                                showSnackError(getString(R.string.error));
                            }
                        }
                    }
                });
    }

    private void unLinkCredentials() {
        getFirebaseAuth().getCurrentUser().unlink(getFirebaseAuth().getCurrentUser().getProviderId())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        linkWithCredentials();
                    }
                });
    }

    private void linkWithCredentials() {
        AuthCredential credential = EmailAuthProvider.getCredential(userMap.get(KEY_EMAIL).toString(),
                userMap.get(KEY_PASSWORD).toString());
        getFirebaseAuth().getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(NumberVerificationActivity.this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        hideLoading();


                        if (task.isSuccessful()) {
                            saveUserData();
                        } else {
                            showSnackError(task.getException().getMessage());
                        }
                    }
                });
    }

    private void saveUserData() {

        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).set(userMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        getAppPreferenceHelper().setUserPhoneNumber(mBinding.codePicker.getFullNumberWithPlus());
                        ActivityUtils.launchActivity(NumberVerificationActivity.this,
                                StudentClassSelect.class);
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

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE && mBinding.codePicker.isValidFullNumber()) {
            phoneNum = mBinding.codePicker.getFullNumberWithPlus();
            mBinding.pinView.setVisibility(View.VISIBLE);
            mBinding.btnResendCode.setVisibility(View.VISIBLE);
            mBinding.btnNext.setVisibility(View.VISIBLE);
            verifyPhoneNumber();
            return true;
        }
        Toast.makeText(NumberVerificationActivity.this, R.string.enter_valid_number, Toast.LENGTH_SHORT).show();
        return false;
    }
}
