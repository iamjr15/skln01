package com.autohub.skln;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.autohub.skln.utills.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.autohub.skln.utills.AppConstants.KEY_ACCOUNT_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.TYPE_STUDENT;
import static com.autohub.skln.utills.AppConstants.TYPE_TUTOR;

public class TutorOrStudent extends BaseActivity {
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra(AppConstants.KEY_DATA);
        phoneNumber = bundle.getString(KEY_PHONE_NUMBER);
        Log.d(">>>Phone", phoneNumber + "");
    }

    // Redirect to the startup of sign up as a Tutor
    @OnClick(R.id.btnTutor)
    public void onTutorClick() {
        verifyUserAndMoveNext(TYPE_TUTOR);
    }

    // Redirect to the startup of sign up as a Student
    @OnClick(R.id.btnStudent)
    public void onStudentClick() {
        verifyUserAndMoveNext(TYPE_STUDENT);
    }

    private void verifyUserAndMoveNext(final String typeStudent) {
        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_ACCOUNT_TYPE, typeStudent);

        getFirebaseStore().collection(getString(R.string.db_root_all_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Intent i = new Intent(TutorOrStudent.this, SignupStart.class);
                        i.putExtra(KEY_ACCOUNT_TYPE, typeStudent);
                        i.putExtra(KEY_PHONE_NUMBER, phoneNumber);
                        startActivity(i);
                        finishAffinity();
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
}
