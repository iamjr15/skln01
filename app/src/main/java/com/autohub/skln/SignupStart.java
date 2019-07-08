package com.autohub.skln;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.autohub.skln.student.StudentClassSelect;
import com.autohub.skln.tutor.TutorCategorySelect;
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
import static com.autohub.skln.utills.AppConstants.KEY_ACCOUNT_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_PASSWORD;
import static com.autohub.skln.utills.AppConstants.KEY_SEX;
import static com.autohub.skln.utills.AppConstants.MALE;
import static com.autohub.skln.utills.AppConstants.TYPE_TUTOR;

public class SignupStart extends BaseActivity {

    @BindView(R.id.tvTutorOrStudent)
    TextView tvTutorOrStudent;

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

    private String mType = TYPE_TUTOR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_signup_start);
        ButterKnife.bind(this);

        mType = getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
        if (mType.equalsIgnoreCase(TYPE_TUTOR))
            tvTutorOrStudent.setText(R.string.tutor);
        else
            tvTutorOrStudent.setText(R.string.student);
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

        final Map<String, Object> user = new HashMap<>();
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

        String dbRoot;
        if (mType.equalsIgnoreCase(TYPE_TUTOR))
            dbRoot = getString(R.string.db_root_tutors);
        else
            dbRoot = getString(R.string.db_root_students);

        getFirebaseStore().collection(dbRoot).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
//                        getAppPreferenceHelper().setTutorName((String) user.get(KEY_FIRST_NAME));
                        if (mType.equalsIgnoreCase(TYPE_TUTOR))
                            startActivity(new Intent(SignupStart.this, TutorCategorySelect.class));
                        else
                            startActivity(new Intent(SignupStart.this, StudentClassSelect.class));
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
