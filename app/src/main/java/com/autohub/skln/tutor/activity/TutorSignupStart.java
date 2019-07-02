package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.FEMALE;
import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_PASSWORD;
import static com.autohub.skln.utills.AppConstants.KEY_SEX;
import static com.autohub.skln.utills.AppConstants.MALE;

public class TutorSignupStart extends BaseActivity {

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtLastName)
    EditText edtLastName;

    @BindView(R.id.sexGroup)
    RadioGroup radioSex;

    @BindView(R.id.radioMale)
    RadioButton radioMale;

    @BindView(R.id.radioFemale)
    RadioButton radioFemale;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_signup_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        if (edtFirstName.getText().length() == 0) {
            edtFirstName.setError(getResources().getString(R.string.enter_name));
            edtFirstName.requestFocus();
            showSnackError(R.string.enter_name);
            return;
        }

        if (edtLastName.getText().length() == 0) {
            edtLastName.setError(getResources().getString(R.string.enter_lastname));
            edtLastName.requestFocus();
            showSnackError(R.string.enter_lastname);
            return;
        }

        if (radioSex.getCheckedRadioButtonId() == -1) {
            radioMale.requestFocus();
            showSnackError(R.string.select_your_sex);
            return;
        }

        if (edtPassword.getText().length() == 0) {
            edtPassword.setError(getResources().getString(R.string.enter_password));
            edtPassword.requestFocus();
            showSnackError(R.string.enter_password);
            return;
        }


        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_FIRST_NAME, edtFirstName.getText().toString());
        user.put(KEY_LAST_NAME, edtLastName.getText().toString());
        user.put(KEY_SEX, radioMale.isChecked() ? MALE : FEMALE);
        try {
            user.put(KEY_PASSWORD, encrypt(edtPassword.getText().toString()));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        getFirebaseStore().collection(getString(R.string.db_root_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        startActivity(new Intent(TutorSignupStart.this, TutorCategorySelect.class));
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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
