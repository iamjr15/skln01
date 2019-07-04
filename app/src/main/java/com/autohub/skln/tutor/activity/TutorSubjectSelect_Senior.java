package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;

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

import static com.autohub.skln.utills.AppConstants.KEY_SUBJECTS;
import static com.autohub.skln.utills.AppConstants.SUBJECT_ACCOUNTANCY;
import static com.autohub.skln.utills.AppConstants.SUBJECT_BIOLOGY;
import static com.autohub.skln.utills.AppConstants.SUBJECT_BUSINESS;
import static com.autohub.skln.utills.AppConstants.SUBJECT_CHEMISTRY;
import static com.autohub.skln.utills.AppConstants.SUBJECT_COMPUTER_SCIENCE;
import static com.autohub.skln.utills.AppConstants.SUBJECT_ECONOMICS;
import static com.autohub.skln.utills.AppConstants.SUBJECT_ENGLISH;
import static com.autohub.skln.utills.AppConstants.SUBJECT_MATHS;
import static com.autohub.skln.utills.AppConstants.SUBJECT_PHYSICS;

public class TutorSubjectSelect_Senior extends BaseActivity {

    @BindView(R.id.ivBiology)
    ImageView ivBiology;

    @BindView(R.id.ivAccountancy)
    ImageView ivAccountancy;

    @BindView(R.id.ivBusiness)
    ImageView ivBusiness;

    @BindView(R.id.ivEconomics)
    ImageView ivEconomics;

    @BindView(R.id.ivEnglish)
    ImageView ivEnglish;

    @BindView(R.id.ivMaths)
    ImageView ivMaths;

    @BindView(R.id.ivPhysics)
    ImageView ivPhysics;

    @BindView(R.id.ivChemistry)
    ImageView ivChemistry;

    @BindView(R.id.ivComputerScience)
    ImageView ivComputerScience;

    private ArrayList<String> selectedSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_subject_senior);
        ButterKnife.bind(this);

        ivBiology.setEnabled(false);
        ivAccountancy.setEnabled(false);
        ivBusiness.setEnabled(false);
        ivEconomics.setEnabled(false);
        ivEnglish.setEnabled(false);
        ivMaths.setEnabled(false);
        ivPhysics.setEnabled(false);
        ivChemistry.setEnabled(false);
        ivComputerScience.setEnabled(false);

        selectedSubjects = new ArrayList<>();
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        StringBuilder stringBuilder = new StringBuilder();
        if (selectedSubjects.size() > 0) {
            stringBuilder.append(selectedSubjects.get(0));
            for (int i = 1; i < selectedSubjects.size(); i++) {
                stringBuilder.append(", ").append(selectedSubjects.get(i));
            }
        }

        if (stringBuilder.length() == 0) {
            showSnackError(R.string.choose_subjects);
            return;
        }

        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_SUBJECTS, stringBuilder.toString());

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        startActivity(new Intent(TutorSubjectSelect_Senior.this, TutorPictureUpload.class));
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

    @OnClick(R.id.LLPhysics)
    public void onPhysicsClick() {
        if (!ivPhysics.isEnabled()) {
            selectedSubjects.add(SUBJECT_PHYSICS);
            ivPhysics.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_PHYSICS);
            ivPhysics.setEnabled(false);
        }
    }

    @OnClick(R.id.LLBiology)
    public void onBiologyClick() {
        if (!ivBiology.isEnabled()) {
            selectedSubjects.add(SUBJECT_BIOLOGY);
            ivBiology.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_BIOLOGY);
            ivBiology.setEnabled(false);
        }
    }

    @OnClick(R.id.LLEnglish)
    public void onEnglishClick() {
        if (!ivEnglish.isEnabled()) {
            selectedSubjects.add(SUBJECT_ENGLISH);
            ivEnglish.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_ENGLISH);
            ivEnglish.setEnabled(false);
        }
    }

    @OnClick(R.id.LLMaths)
    public void onMathsClick() {
        if (!ivMaths.isEnabled()) {
            selectedSubjects.add(SUBJECT_MATHS);
            ivMaths.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_MATHS);
            ivMaths.setEnabled(false);
        }
    }

    @OnClick(R.id.LLChemistry)
    public void onChemistryClick() {
        if (!ivChemistry.isEnabled()) {
            selectedSubjects.add(SUBJECT_CHEMISTRY);
            ivChemistry.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_CHEMISTRY);
            ivChemistry.setEnabled(false);
        }
    }

    @OnClick(R.id.LLComputerScience)
    public void onComputerClick() {
        if (!ivComputerScience.isEnabled()) {
            selectedSubjects.add(SUBJECT_COMPUTER_SCIENCE);
            ivComputerScience.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_COMPUTER_SCIENCE);
            ivComputerScience.setEnabled(false);
        }
    }

    @OnClick(R.id.LLAccountancy)
    public void onAccountacyClick() {
        if (!ivAccountancy.isEnabled()) {
            selectedSubjects.add(SUBJECT_ACCOUNTANCY);
            ivAccountancy.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_ACCOUNTANCY);
            ivAccountancy.setEnabled(false);
        }
    }

    @OnClick(R.id.LLEconomics)
    public void onEconomicsClick() {
        if (!ivEconomics.isEnabled()) {
            selectedSubjects.add(SUBJECT_ECONOMICS);
            ivEconomics.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_ECONOMICS);
            ivEconomics.setEnabled(false);
        }
    }

    @OnClick(R.id.LLBusiness)
    public void onBusinessClick() {
        if (!ivBusiness.isEnabled()) {
            selectedSubjects.add(SUBJECT_BUSINESS);
            ivBusiness.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_BUSINESS);
            ivBusiness.setEnabled(false);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
