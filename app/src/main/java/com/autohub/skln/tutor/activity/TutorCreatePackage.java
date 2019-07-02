package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.autohub.skln.BaseActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.autohub.skln.R;
import com.autohub.skln.utills.AppConstants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_AREA_QUALIFICATION;
import static com.autohub.skln.utills.AppConstants.KEY_BOARD;
import static com.autohub.skln.utills.AppConstants.KEY_CLASS_FREQUENCY;
import static com.autohub.skln.utills.AppConstants.KEY_CLASS_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_MAX_STUDENTS;
import static com.autohub.skln.utills.AppConstants.KEY_NO_OF_CLASSES;
import static com.autohub.skln.utills.AppConstants.KEY_QUALIFICATION;

public class TutorCreatePackage extends BaseActivity {
    private static final String TAG = "TutorCreatePackage";

    @BindView(R.id.tvClassType)
    TextView tvClassType;

    @BindView(R.id.tvClassNumber)
    TextView tvClassNumber;

    @BindView(R.id.tvClassFreq)
    TextView tvClassFreq;

    @BindView(R.id.tvMaxStudents)
    TextView tvMaxStudents;

    private String[] classTypes;
    private String[] classNumbers = new String[30];
    private String[] classFreqs;
    private String[] maxStudents = new String[39];

    private String mSelectedClassType = "";
    private int mSelectedClassNumber = -1;
    private String mSelectedClassFreq = "";
    private int mSelectedMaxStudents = -1;

    private PopupWindow popupClassType;
    private PopupWindow popupClassNumber;
    private PopupWindow popupClassFreq;
    private PopupWindow popupMaxStudents;

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_create_package);
        ButterKnife.bind(this);

        classTypes = getResources().getStringArray(R.array.class_types_array);
        classFreqs = getResources().getStringArray(R.array.class_freqs_array);
        for (int i = 0; i < 30; i ++) {
            classNumbers[i] = "" + (i + 1);
        }
        for (int i = 0; i < 39; i ++) {
            maxStudents[i] = "" + (i + 2);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels;
    }

    @OnClick(R.id.tvClassType)
    public void onClassTypeClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
        img.setBounds(0, 0, 120, 120);
        tvClassType.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = createPopupClassType();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow createPopupClassType() {
        popupClassType = new PopupWindow(this);
        popupClassType.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items, classTypes);
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvClassType.setText(mSelectedClassType = classTypes[position]);
                tvClassType.setCompoundDrawables(null, null, img, null);

                if (mSelectedClassType.equals(classTypes[0])) {
                    if (tvMaxStudents.getVisibility() == View.GONE) {
                        tvMaxStudents.setVisibility(View.VISIBLE);
                        tvMaxStudents.setText(R.string.specify_max_students);
                    }
                } else {
                    tvMaxStudents.setVisibility(View.GONE);
                    mSelectedMaxStudents = -1;
                }

                if (popupClassType != null) {
                    popupClassType.dismiss();
                }
            }
        });
        setPopWidth(popupClassType);
        popupClassType.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvClassType.setCompoundDrawables(null, null, img, null);
            }
        });
        popupClassType.setFocusable(true);
        popupClassType.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupClassType.setContentView(listViewSort);
        return popupClassType;
    }

    @OnClick(R.id.tvClassNumber)
    public void onClassNumberClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
        img.setBounds(0, 0, 120, 120);
        tvClassNumber.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = createPopupClassNumber();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow createPopupClassNumber() {
        popupClassNumber = new PopupWindow(this);
        popupClassNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, classNumbers);
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvClassNumber.setText("" + (mSelectedClassNumber = Integer.parseInt(classNumbers[position])));
                tvClassNumber.setCompoundDrawables(null, null, img, null);

                if (popupClassNumber != null) {
                    popupClassNumber.dismiss();
                }
            }
        });
        setPopWidth(popupClassNumber);
        popupClassNumber.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvClassNumber.setCompoundDrawables(null, null, img, null);
            }
        });
        popupClassNumber.setFocusable(true);
        popupClassNumber.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupClassNumber.setContentView(listViewSort);
        return popupClassNumber;
    }

    @OnClick(R.id.tvClassFreq)
    public void onClassFreqClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
        img.setBounds(0, 0, 120, 120);
        tvClassFreq.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = createPopupClassFreq();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow createPopupClassFreq() {
        popupClassFreq = new PopupWindow(this);
        popupClassFreq.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, maxStudents);
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvClassFreq.setText(mSelectedClassFreq = classFreqs[position]);
                tvClassFreq.setCompoundDrawables(null, null, img, null);

                if (popupClassFreq != null) {
                    popupClassFreq.dismiss();
                }
            }
        });
        setPopWidth(popupClassFreq);
        popupClassFreq.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvClassFreq.setCompoundDrawables(null, null, img, null);
            }
        });
        popupClassFreq.setFocusable(true);
        popupClassFreq.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupClassFreq.setContentView(listViewSort);
        return popupClassFreq;
    }

    @OnClick(R.id.tvMaxStudents)
    public void onMaxStudentsClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
        img.setBounds(0, 0, 120, 120);
        tvMaxStudents.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = createPopupMaxStudents();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow createPopupMaxStudents() {
        popupMaxStudents = new PopupWindow(this);
        popupMaxStudents.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, maxStudents);
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvMaxStudents.setText("" + (mSelectedMaxStudents = Integer.parseInt(maxStudents[position])));
                tvMaxStudents.setCompoundDrawables(null, null, img, null);

                if (popupMaxStudents != null) {
                    popupMaxStudents.dismiss();
                }
            }
        });
        setPopWidth(popupMaxStudents);
        popupMaxStudents.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                img.setBounds(0, 0, 120, 120);
                tvMaxStudents.setCompoundDrawables(null, null, img, null);
            }
        });
        popupMaxStudents.setFocusable(true);
        popupMaxStudents.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupMaxStudents.setContentView(listViewSort);
        return popupMaxStudents;
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        if (TextUtils.isEmpty(mSelectedClassType)) {
            showSnackError(R.string.select_class_type);
            return;
        }

        if (mSelectedClassNumber == -1) {
            showSnackError(R.string.select_number_of_class);
            return;
        }

        if (TextUtils.isEmpty(mSelectedClassFreq)) {
            showSnackError(R.string.select_class_freq);
            return;
        }

        if (mSelectedMaxStudents == -1 && tvMaxStudents.getVisibility() == View.VISIBLE) {
            showSnackError(R.string.select_max_students_capacity);
            return;
        }

        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_CLASS_TYPE, mSelectedClassType);
        user.put(KEY_NO_OF_CLASSES, mSelectedClassNumber);
        user.put(KEY_CLASS_FREQUENCY, mSelectedClassFreq);
        user.put(KEY_MAX_STUDENTS, mSelectedMaxStudents);

        getFirebaseStore().collection(getString(R.string.db_root_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        startActivity(new Intent(TutorCreatePackage.this, TutorHomeActivity.class));
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

    private void setPopWidth(PopupWindow popupWindow) {
        if (width >= 1440) {
            popupWindow.setWidth(width - 200);
        } else if (width >= 1080) {
            popupWindow.setWidth(width - 150);
        } else if (width >= 720) {
            popupWindow.setWidth(width - 100);
        } else {
            popupWindow.setWidth(width - 100);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
