package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.autohub.skln.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.autohub.skln.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TutorOrStudent extends BaseActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        /*
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, TutorHomeActivity.class));
            finish();
        }
*/

    }

    /*Redirect to the login Activity*/
    @OnClick(R.id.btnTutor)
    public void onTutorClick() {
        startActivity(new Intent(this, TutorSignupStart.class));
        finish();
    }

    /*Redirect to the Registration Activity*/
    @OnClick(R.id.btnStudent)
    public void onStudentClick() {
        /*startActivity(new Intent(this, TutorSignupStart.class));
        finish();*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
