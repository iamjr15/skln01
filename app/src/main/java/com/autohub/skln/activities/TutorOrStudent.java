package com.autohub.skln.activities;

import android.os.Bundle;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.activities.tutor.TutorSignupActivity;
import com.autohub.skln.activities.user.SignupStart;
import com.autohub.skln.utills.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.autohub.skln.utills.AppConstants.TYPE_STUDENT;

public class TutorOrStudent extends BaseActivity {
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_or_student);
        ButterKnife.bind(this);
    }

    // Redirect to the startup of sign up as a Tutor
    @OnClick(R.id.btnTutor)
    public void onTutorClick() {
        ActivityUtils.launchActivity(TutorOrStudent.this, TutorSignupActivity.class);
    }

    // Redirect to the startup of sign up as a Student
    @OnClick(R.id.btnStudent)
    public void onStudentClick() {
        ActivityUtils.launchActivity(TutorOrStudent.this, SignupStart.class);
        verifyUserAndMoveNext(TYPE_STUDENT);
    }

    private void verifyUserAndMoveNext(final String typeStudent) {

//        if(typeStudent)



       /* showLoading();

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
                });*/
    }
}
