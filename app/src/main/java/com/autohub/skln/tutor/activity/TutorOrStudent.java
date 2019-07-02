package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.autohub.skln.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.autohub.skln.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_ACCOUNT_TYPE;
import static com.autohub.skln.utills.AppConstants.TYPE_TUTOR;

public class TutorOrStudent extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        ButterKnife.bind(this);
    }

    // Redirect to the startup of sign up as a Tutor
    @OnClick(R.id.btnTutor)
    public void onTutorClick() {
        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_ACCOUNT_TYPE, TYPE_TUTOR);

        getFirebaseStore().collection(getString(R.string.db_root_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        startActivity(new Intent(TutorOrStudent.this, TutorSignupStart.class));
                        finish();
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

    // Redirect to the startup of sign up as a Student
    @OnClick(R.id.btnStudent)
    public void onStudentClick() {
        /*startActivity(new Intent(this, StudentSignupStart.class));
        finish();*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
