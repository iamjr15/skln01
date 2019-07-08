package com.autohub.skln.tutor;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.autohub.skln.BaseActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;
import com.autohub.skln.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.KEY_CLASS_FREQUENCY;
import static com.autohub.skln.utills.AppConstants.KEY_CLASS_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_MAX_STUDENTS;
import static com.autohub.skln.utills.AppConstants.KEY_NO_OF_CLASSES;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.KEY_RATE;

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

    @BindView(R.id.edtRate)
    EditText edtRate;

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
        tvClassType.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
                tvClassType.setText(mSelectedClassType = classTypes[position]);
                tvClassType.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
                tvClassType.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
        tvClassNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
                tvClassNumber.setText("" + (mSelectedClassNumber = Integer.parseInt(classNumbers[position])));
                tvClassNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
                tvClassNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
        tvClassFreq.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
        PopupWindow popUp = createPopupClassFreq();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow createPopupClassFreq() {
        popupClassFreq = new PopupWindow(this);
        popupClassFreq.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, classFreqs);
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.drop_down_arrow);
                tvClassFreq.setText(mSelectedClassFreq = classFreqs[position]);
                tvClassFreq.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
                tvClassFreq.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
        tvMaxStudents.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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
                tvMaxStudents.setText("" + (mSelectedMaxStudents = Integer.parseInt(maxStudents[position])));
                tvMaxStudents.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
                tvMaxStudents.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
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

        if (TextUtils.isEmpty(edtRate.getText().toString())) {
            showSnackError(R.string.input_rate);
            return;
        }

        showLoading();

        final Map<String, Object> user = new HashMap<>();
        user.put(KEY_CLASS_TYPE, mSelectedClassType);
        user.put(KEY_NO_OF_CLASSES, mSelectedClassNumber);
        user.put(KEY_CLASS_FREQUENCY, mSelectedClassFreq);
        user.put(KEY_MAX_STUDENTS, mSelectedMaxStudents);
        user.put(KEY_RATE, edtRate.getText().toString());

        final Map<String, Object> user1 = new HashMap<>();
        user1.put(KEY_PHONE_NUMBER, getAppPreferenceHelper().getUserPhone());

        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        getFirebaseStore().collection(getString(R.string.db_root_all_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user1, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        hideLoading();

                                        Intent i = new Intent(TutorCreatePackage.this, TutorHomeActivity.class);
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
