package com.autohub.skln.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;

public class StudentHey extends BaseActivity {

    @BindView(R.id.student_name)
    TextView tvName;

    private boolean mIsSeniorClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_hey);
        ButterKnife.bind(this);

        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String firstName = documentSnapshot.getString(KEY_FIRST_NAME);
                        tvName.setText(firstName);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showSnackError(e.getMessage());
                    }
                });

        mIsSeniorClass = getIntent().getBooleanExtra("is_senior", false);
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        Intent i;
        if (mIsSeniorClass)
            i = new Intent(this, StudentSubjectSelect_Senior.class);
        else
            i = new Intent(this, StudentSubjectSelect.class);

        i.putExtra("favorite_or_least", true);
        startActivity(i);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
