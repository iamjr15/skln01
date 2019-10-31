package com.autohub.skln.tutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.CATEGORY_ACADEMICS;
import static com.autohub.skln.utills.AppConstants.CATEGORY_HOBBY;
import static com.autohub.skln.utills.AppConstants.KEY_CATEGORY;
import static com.autohub.skln.utills.AppConstants.KEY_USER_ID;

public class TutorCategorySelect extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_category_select);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rlAcademics)
    public void onAcademicClick() {
        getAppPreferenceHelper().setTutorCategory(CATEGORY_ACADEMICS);
        goToNext(true);
    }

    @OnClick(R.id.rlHobby)
    public void onHobbyClick() {
        getAppPreferenceHelper().setTutorCategory(CATEGORY_HOBBY);
        goToNext(false);
    }

    private void goToNext(final boolean toAcademics) {
        showLoading();
        Map<String, Object> user = new HashMap<>();
        user.put(KEY_CATEGORY, toAcademics ? CATEGORY_ACADEMICS : CATEGORY_HOBBY);
        String uid = getFirebaseAuth().getCurrentUser().getUid();
        user.put(KEY_USER_ID, uid);

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(uid).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        if (toAcademics)
                            startActivity(new Intent(TutorCategorySelect.this, TutorClassSelect.class));
                        else
                            startActivity(new Intent(TutorCategorySelect.this, TutorHobbySelect.class));
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
