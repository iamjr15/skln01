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

import static com.autohub.skln.utills.AppConstants.CLASS_1;
import static com.autohub.skln.utills.AppConstants.CLASS_10;
import static com.autohub.skln.utills.AppConstants.CLASS_11;
import static com.autohub.skln.utills.AppConstants.CLASS_12;
import static com.autohub.skln.utills.AppConstants.CLASS_2;
import static com.autohub.skln.utills.AppConstants.CLASS_3;
import static com.autohub.skln.utills.AppConstants.CLASS_4;
import static com.autohub.skln.utills.AppConstants.CLASS_5;
import static com.autohub.skln.utills.AppConstants.CLASS_6;
import static com.autohub.skln.utills.AppConstants.CLASS_7;
import static com.autohub.skln.utills.AppConstants.CLASS_8;
import static com.autohub.skln.utills.AppConstants.CLASS_9;
import static com.autohub.skln.utills.AppConstants.KEY_CLASSES;

public class TutorClassSelect extends BaseActivity {

    @BindView(R.id.ivClass1)
    ImageView ivClass1;

    @BindView(R.id.ivClass2)
    ImageView ivClass2;

    @BindView(R.id.ivClass3)
    ImageView ivClass3;

    @BindView(R.id.ivClass4)
    ImageView ivClass4;

    @BindView(R.id.ivClass5)
    ImageView ivClass5;

    @BindView(R.id.ivClass6)
    ImageView ivClass6;

    @BindView(R.id.ivClass7)
    ImageView ivClass7;

    @BindView(R.id.ivClass8)
    ImageView ivClass8;

    @BindView(R.id.ivClass9)
    ImageView ivClass9;

    @BindView(R.id.ivClass10)
    ImageView ivClass10;

    @BindView(R.id.ivClass11)
    ImageView ivClass11;

    @BindView(R.id.ivClass12)
    ImageView ivClass12;

    private ArrayList<String> selectedClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_class_select);
        ButterKnife.bind(this);

        ivClass1.setEnabled(false);
        ivClass2.setEnabled(false);
        ivClass3.setEnabled(false);
        ivClass4.setEnabled(false);
        ivClass5.setEnabled(false);
        ivClass6.setEnabled(false);
        ivClass7.setEnabled(false);
        ivClass8.setEnabled(false);
        ivClass9.setEnabled(false);
        ivClass10.setEnabled(false);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);

        selectedClasses = new ArrayList<>();
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        StringBuilder stringBuilder = new StringBuilder();
        int size = selectedClasses.size();
        if (size > 0) {
            stringBuilder.append(selectedClasses.get(0));
            for (int i = 1; i < selectedClasses.size(); i++) {
                stringBuilder.append(", ").append(selectedClasses.get(i));
            }
        }

        if (stringBuilder.length() == 0) {
            showSnackError(R.string.choose_classes);
            return;
        }

        showLoading();

        final boolean isSeniorClass = selectedClasses.contains(CLASS_11) || selectedClasses.contains(CLASS_12);

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_CLASSES, stringBuilder.toString());

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        if (isSeniorClass) {
                            startActivity(new Intent(TutorClassSelect.this, TutorSubjectSelect_Senior.class));
                        } else {
                            startActivity(new Intent(TutorClassSelect.this, TutorSubjectSelect.class));
                        }
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

    @OnClick(R.id.LLClass1)
    public void onClass1Click() {
        if (!ivClass1.isEnabled()) {
            ivClass1.setEnabled(true);
            selectedClasses.add(CLASS_1);
        } else {
            ivClass1.setEnabled(false);
            selectedClasses.remove(CLASS_1);
        }

        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }

        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass2)
    public void onClass2Click() {
        if (!ivClass2.isEnabled()) {
            selectedClasses.add(CLASS_2);
            ivClass2.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_2);
            ivClass2.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass3)
    public void onClass3Click() {
        if (!ivClass3.isEnabled()) {
            selectedClasses.add(CLASS_3);
            ivClass3.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_3);
            ivClass3.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass4)
    public void onClass4Click() {
        if (!ivClass4.isEnabled()) {
            selectedClasses.add(CLASS_4);
            ivClass4.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_4);
            ivClass4.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass5)
    public void onClass5Click() {
        if (!ivClass5.isEnabled()) {
            selectedClasses.add(CLASS_5);
            ivClass5.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_5);
            ivClass5.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass6)
    public void onClass6Click() {
        if (!ivClass6.isEnabled()) {
            selectedClasses.add(CLASS_6);
            ivClass6.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_6);
            ivClass6.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass7)
    public void onClass7Click() {
        if (!ivClass7.isEnabled()) {
            selectedClasses.add(CLASS_7);
            ivClass7.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_7);
            ivClass7.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass8)
    public void onClass8Click() {
        if (!ivClass8.isEnabled()) {
            selectedClasses.add(CLASS_8);
            ivClass8.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_8);
            ivClass8.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass9)
    public void onClass9Click() {
        if (!ivClass9.isEnabled()) {
            selectedClasses.add(CLASS_9);
            ivClass9.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_9);
            ivClass9.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }

        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass10)
    public void onClass10Click() {
        if (!ivClass10.isEnabled()) {
            selectedClasses.add(CLASS_10);
            ivClass10.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_10);
            ivClass10.setEnabled(false);
        }
        if (ivClass11.isEnabled() || ivClass12.isEnabled()) {
            showSnackError(R.string.class_selection_warning);
        }
        selectedClasses.remove(CLASS_11);
        selectedClasses.remove(CLASS_12);
        ivClass11.setEnabled(false);
        ivClass12.setEnabled(false);
    }

    @OnClick(R.id.LLClass11)
    public void onClass11Click() {
        if (!ivClass11.isEnabled()) {
            selectedClasses.add(CLASS_11);
            ivClass11.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_11);
            ivClass11.setEnabled(false);
        }
        selectedClasses.remove(CLASS_1);
        selectedClasses.remove(CLASS_2);
        selectedClasses.remove(CLASS_3);
        selectedClasses.remove(CLASS_4);
        selectedClasses.remove(CLASS_5);
        selectedClasses.remove(CLASS_6);
        selectedClasses.remove(CLASS_7);
        selectedClasses.remove(CLASS_8);
        selectedClasses.remove(CLASS_9);
        selectedClasses.remove(CLASS_10);
        ivClass1.setEnabled(false);
        ivClass2.setEnabled(false);
        ivClass3.setEnabled(false);
        ivClass4.setEnabled(false);
        ivClass5.setEnabled(false);
        ivClass6.setEnabled(false);
        ivClass7.setEnabled(false);
        ivClass8.setEnabled(false);
        ivClass9.setEnabled(false);
        ivClass10.setEnabled(false);
    }

    @OnClick(R.id.LLClass12)
    public void onClass12Click() {
        if (!ivClass12.isEnabled()) {
            selectedClasses.add(CLASS_12);
            ivClass12.setEnabled(true);
        } else {
            selectedClasses.remove(CLASS_12);
            ivClass12.setEnabled(false);
        }
        selectedClasses.remove(CLASS_1);
        selectedClasses.remove(CLASS_2);
        selectedClasses.remove(CLASS_3);
        selectedClasses.remove(CLASS_4);
        selectedClasses.remove(CLASS_5);
        selectedClasses.remove(CLASS_6);
        selectedClasses.remove(CLASS_7);
        selectedClasses.remove(CLASS_8);
        selectedClasses.remove(CLASS_9);
        selectedClasses.remove(CLASS_10);
        ivClass1.setEnabled(false);
        ivClass2.setEnabled(false);
        ivClass3.setEnabled(false);
        ivClass4.setEnabled(false);
        ivClass5.setEnabled(false);
        ivClass6.setEnabled(false);
        ivClass7.setEnabled(false);
        ivClass8.setEnabled(false);
        ivClass9.setEnabled(false);
        ivClass10.setEnabled(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
