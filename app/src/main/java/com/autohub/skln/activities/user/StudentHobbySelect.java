package com.autohub.skln.activities.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.HOBBY_DANCE;
import static com.autohub.skln.utills.AppConstants.HOBBY_DRUM;
import static com.autohub.skln.utills.AppConstants.HOBBY_GUITAR;
import static com.autohub.skln.utills.AppConstants.HOBBY_KEYBOARD;
import static com.autohub.skln.utills.AppConstants.HOBBY_MARTIAL;
import static com.autohub.skln.utills.AppConstants.HOBBY_PAINT;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.KEY_STDT_HOBBIES;

public class StudentHobbySelect extends BaseActivity {

    @BindView(R.id.ivGuitar)
    ImageView ivGUitar;

    @BindView(R.id.ivpainting)
    ImageView ivPainting;

    @BindView(R.id.ivMartialArts)
    ImageView ivMartialArts;

    @BindView(R.id.ivDrum)
    ImageView ivDrum;

    @BindView(R.id.ivKeyboard)
    ImageView ivKeyboard;

    @BindView(R.id.ivDance)
    ImageView ivDance;

    private ArrayList<String> selectedHobbies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_hobby_select);
        ButterKnife.bind(this);

        ivGUitar.setEnabled(false);
        ivPainting.setEnabled(false);
        ivMartialArts.setEnabled(false);
        ivDance.setEnabled(false);
        ivDrum.setEnabled(false);
        ivKeyboard.setEnabled(false);

        selectedHobbies = new ArrayList<>();
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        StringBuilder stringBuilder = new StringBuilder();
        if (selectedHobbies.size() > 0) {
            stringBuilder.append(selectedHobbies.get(0));
            for (int i = 1; i < selectedHobbies.size(); i++) {
                stringBuilder.append(", ").append(selectedHobbies.get(i));
            }
        }

        if (stringBuilder.length() == 0) {
            showSnackError(R.string.choose_hobbies);
            return;
        }

        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_STDT_HOBBIES, stringBuilder.toString());

        final Map<String, Object> user1 = new HashMap<>();
        user1.put(KEY_PHONE_NUMBER, getAppPreferenceHelper().getUserPhone());

        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        getFirebaseStore().collection(getString(R.string.db_root_all_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user1, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        hideLoading();
                                        getAppPreferenceHelper().setSignupComplete(true);
                                        Intent i = new Intent(StudentHobbySelect.this, StudentHomeActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
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
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideLoading();
                        showSnackError(e.getMessage());
                    }
                });
    }

    @OnClick(R.id.LLGuitar)
    public void onGuitarClick() {
        if (!ivGUitar.isEnabled()) {
            selectedHobbies.add(HOBBY_GUITAR);
            ivGUitar.setEnabled(true);
        } else {
            selectedHobbies.remove(HOBBY_GUITAR);
            ivGUitar.setEnabled(false);
        }
    }

    @OnClick(R.id.LLPainting)
    public void onPaintingClick() {
        if (!ivPainting.isEnabled()) {
            selectedHobbies.add(HOBBY_PAINT);
            ivPainting.setEnabled(true);
        } else {
            selectedHobbies.remove(HOBBY_PAINT);
            ivPainting.setEnabled(false);
        }
    }

    @OnClick(R.id.LLMartialArts)
    public void onMartialArtClick() {
        if (!ivMartialArts.isEnabled()) {
            selectedHobbies.add(HOBBY_MARTIAL);
            ivMartialArts.setEnabled(true);
        } else {
            selectedHobbies.remove(HOBBY_MARTIAL);
            ivMartialArts.setEnabled(false);
        }
    }

    @OnClick(R.id.LLDrum)
    public void onDrumClick() {
        if (!ivDrum.isEnabled()) {
            selectedHobbies.add(HOBBY_DRUM);
            ivDrum.setEnabled(true);
        } else {
            selectedHobbies.remove(HOBBY_DRUM);
            ivDrum.setEnabled(false);
        }
    }

    @OnClick(R.id.LLkeyboard)
    public void onKeyBoardClick() {
        if (!ivKeyboard.isEnabled()) {
            selectedHobbies.add(HOBBY_KEYBOARD);
            ivKeyboard.setEnabled(true);
        } else {
            selectedHobbies.remove(HOBBY_KEYBOARD);
            ivKeyboard.setEnabled(false);
        }
    }

    @OnClick(R.id.LLDance)
    public void onDanceClick() {
        if (!ivDance.isEnabled()) {
            selectedHobbies.add(HOBBY_DANCE);
            ivDance.setEnabled(true);
        } else {
            selectedHobbies.remove(HOBBY_DANCE);
            ivDance.setEnabled(false);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
