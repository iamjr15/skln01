package com.autohub.skln.activities.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

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
import static com.autohub.skln.utills.AppConstants.KEY_STDT_CLASS;
import static com.autohub.skln.utills.AppConstants.KEY_USER_ID;

public class StudentClassSelect extends BaseActivity {

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

    private String selectedClass;
    private SparseArray<ImageView> ivClasses = new SparseArray<>();
    private SparseArray<String> CLASSES = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_class_select);
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

        ivClasses.put(R.id.LLClass1, ivClass1);
        ivClasses.put(R.id.LLClass2, ivClass2);
        ivClasses.put(R.id.LLClass3, ivClass3);
        ivClasses.put(R.id.LLClass4, ivClass4);
        ivClasses.put(R.id.LLClass5, ivClass5);
        ivClasses.put(R.id.LLClass6, ivClass6);
        ivClasses.put(R.id.LLClass7, ivClass7);
        ivClasses.put(R.id.LLClass8, ivClass8);
        ivClasses.put(R.id.LLClass9, ivClass9);
        ivClasses.put(R.id.LLClass10, ivClass10);
        ivClasses.put(R.id.LLClass11, ivClass11);
        ivClasses.put(R.id.LLClass12, ivClass12);

        CLASSES.put(R.id.LLClass1, CLASS_1);
        CLASSES.put(R.id.LLClass2, CLASS_2);
        CLASSES.put(R.id.LLClass3, CLASS_3);
        CLASSES.put(R.id.LLClass4, CLASS_4);
        CLASSES.put(R.id.LLClass5, CLASS_5);
        CLASSES.put(R.id.LLClass6, CLASS_6);
        CLASSES.put(R.id.LLClass7, CLASS_7);
        CLASSES.put(R.id.LLClass8, CLASS_8);
        CLASSES.put(R.id.LLClass9, CLASS_9);
        CLASSES.put(R.id.LLClass10, CLASS_10);
        CLASSES.put(R.id.LLClass11, CLASS_11);
        CLASSES.put(R.id.LLClass12, CLASS_12);
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {

        if (TextUtils.isEmpty(selectedClass)) {
            showSnackError(R.string.select_class_text_1);
            return;
        }

        showLoading();

        final boolean isSeniorClass = selectedClass.equalsIgnoreCase(CLASS_11) || selectedClass.equalsIgnoreCase(CLASS_12);

        Map<String, Object> user = new HashMap<>();
        String uid = getFirebaseAuth().getCurrentUser().getUid();
        user.put(KEY_STDT_CLASS, selectedClass);
        user.put(KEY_USER_ID, uid);

        getFirebaseStore().collection(getString(R.string.db_root_students)).document(uid).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        Intent i = new Intent(StudentClassSelect.this, StudentHey.class);
                        i.putExtra("is_senior", isSeniorClass);
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

    @OnClick({R.id.LLClass1, R.id.LLClass2, R.id.LLClass3, R.id.LLClass4, R.id.LLClass5, R.id.LLClass6, R.id.LLClass7, R.id.LLClass8, R.id.LLClass9, R.id.LLClass10, R.id.LLClass11, R.id.LLClass12})
    public void onClassItemClick(View view) {
        for (int i = 0; i < ivClasses.size(); i++) {
            int key = ivClasses.keyAt(i);
            ImageView ivClass = ivClasses.get(key);

            if (key == view.getId()) {
                ivClass.setEnabled(true);
                selectedClass = CLASSES.get(key);
            } else {
                ivClass.setEnabled(false);
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
