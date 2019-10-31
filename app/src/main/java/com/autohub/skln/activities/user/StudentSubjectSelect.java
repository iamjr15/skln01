package com.autohub.skln.activities.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

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

import static com.autohub.skln.utills.AppConstants.KEY_STDT_FAVORITE_CLASSES;
import static com.autohub.skln.utills.AppConstants.KEY_STDT_LEAST_FAV_CLASSES;
import static com.autohub.skln.utills.AppConstants.SUBJECT_COMPUTER_SCIENCE;
import static com.autohub.skln.utills.AppConstants.SUBJECT_ENGLISH;
import static com.autohub.skln.utills.AppConstants.SUBJECT_LANGUAGES;
import static com.autohub.skln.utills.AppConstants.SUBJECT_MATHS;
import static com.autohub.skln.utills.AppConstants.SUBJECT_SCIENCE;
import static com.autohub.skln.utills.AppConstants.SUBJECT_SOCIAL_STUDIES;

public class StudentSubjectSelect extends BaseActivity {

    @BindView(R.id.tvSelectText)
    TextView tvSelectText;

    @BindView(R.id.ivScience)
    ImageView ivScience;

    @BindView(R.id.ivEnglish)
    ImageView ivEnglish;

    @BindView(R.id.ivMaths)
    ImageView ivMaths;

    @BindView(R.id.ivSocialStudies)
    ImageView ivSocialStudies;

    @BindView(R.id.pbSignupProgress)
    ProgressBar pbSignupProgress;

    @BindView(R.id.ivLanguages)
    ImageView ivLanguages;

    @BindView(R.id.ivComputerScience)
    ImageView ivComputerScience;

    private ArrayList<String> selectedSubjects;

    private boolean mFavoriteOrLeast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_subject_select);
        ButterKnife.bind(this);

        ivScience.setEnabled(false);
        ivEnglish.setEnabled(false);
        ivMaths.setEnabled(false);
        ivSocialStudies.setEnabled(false);
        ivLanguages.setEnabled(false);
        ivComputerScience.setEnabled(false);

        selectedSubjects = new ArrayList<>();

        mFavoriteOrLeast = getIntent().getBooleanExtra("favorite_or_least", true);

        if (mFavoriteOrLeast) {
            SpannableStringBuilder spannable = new SpannableStringBuilder(getResources().getString(R.string.select_favorite_subject));

            spannable.setSpan(new ForegroundColorSpan(Color.BLUE), 12, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannable.setSpan(new UnderlineSpan(), 12, 21, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tvSelectText.setText(spannable, TextView.BufferType.SPANNABLE);
            pbSignupProgress.setProgress(3);
        } else {
            pbSignupProgress.setProgress(4);
            SpannableStringBuilder spannable = new SpannableStringBuilder(getResources().getString(R.string.select_least_favorite_subject));
            spannable.setSpan(new ForegroundColorSpan(Color.RED), 12, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannable.setSpan(new UnderlineSpan(), 12, 27, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tvSelectText.setText(spannable, TextView.BufferType.SPANNABLE);
        }
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
        if (mFavoriteOrLeast)
            user.put(KEY_STDT_FAVORITE_CLASSES, stringBuilder.toString());
        else
            user.put(KEY_STDT_LEAST_FAV_CLASSES, stringBuilder.toString());

        getFirebaseStore().collection(getString(R.string.db_root_students)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Intent i;
                        if (mFavoriteOrLeast) {
                            i = new Intent(StudentSubjectSelect.this, StudentSubjectSelect.class);
                            i.putExtra("favorite_or_least", false);
                        } else {
                            i = new Intent(StudentSubjectSelect.this, StudentHobbySelect.class);
                        }
                        startActivity(i);
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

    @OnClick(R.id.LLScience)
    public void onScienceClick() {
        if (!ivScience.isEnabled()) {
            selectedSubjects.add(SUBJECT_SCIENCE);
            ivScience.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_SCIENCE);
            ivScience.setEnabled(false);
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

    @OnClick(R.id.LLSocialStudies)
    public void onSocialClick() {
        if (!ivSocialStudies.isEnabled()) {
            selectedSubjects.add(SUBJECT_SOCIAL_STUDIES);
            ivSocialStudies.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_SOCIAL_STUDIES);
            ivSocialStudies.setEnabled(false);
        }
    }

    @OnClick(R.id.LLLanguages)
    public void onLanguagesClick() {
        if (!ivLanguages.isEnabled()) {
            selectedSubjects.add(SUBJECT_LANGUAGES);
            ivLanguages.setEnabled(true);
        } else {
            selectedSubjects.remove(SUBJECT_LANGUAGES);
            ivLanguages.setEnabled(false);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
