package com.autohub.skln.tutor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.databinding.ActivityTutorCreatePackageBinding;
import com.autohub.skln.listeners.ItemSelectedListenerWrapper;
import com.autohub.skln.utills.AppConstants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TutorCreatePackage extends BaseActivity {
    private String[] classNumbers = new String[31];
    private String[] maxStudents = new String[41];
    private ActivityTutorCreatePackageBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tutor_create_package);
        mBinding.setCallback(this);
        classNumbers[0] = "NO. OF CLASSES PER WEEK/MONTH";
        for (int i = 1; i <= 30; i++) {
            classNumbers[i] = String.valueOf(i);
        }
        maxStudents[0] = "MAX. STUDENT CAPACITY";
        for (int i = 1; i <= 40; i++) {
            maxStudents[i] = String.valueOf(i);
        }
        mBinding.classType.setAdapter(new SpinnerAdapter(this, getResources().getStringArray(R.array.class_types_array)));
        mBinding.numbOfClasses.setAdapter(new SpinnerAdapter(this, classNumbers));
        mBinding.classFrequency.setAdapter(new SpinnerAdapter(this, getResources().getStringArray(R.array.class_freqs_array)));
        mBinding.maxStudentCapacity.setAdapter(new SpinnerAdapter(this, maxStudents));
        mBinding.paymentDuration.setAdapter(new SpinnerAdapter(this, getResources().getStringArray(R.array.payment_duration), R.color.blue));
        mBinding.classType.setOnItemSelectedListener(new ItemSelectedListenerWrapper() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getAdapter().getItem(position);
                mBinding.maxStudentCapacity.setVisibility("Group class".equalsIgnoreCase(item) ? View.VISIBLE : View.GONE);
            }
        });
    }

    public void onNextClick() {
        if (!isValid()) return;
        showLoading();
        final Map<String, Object> user = new HashMap<>();
        user.put(AppConstants.KEY_CLASS_TYPE, mBinding.classType.getSelectedItem().toString());
        user.put(AppConstants.KEY_NO_OF_CLASSES, mBinding.numbOfClasses.getSelectedItem().toString());
        user.put(AppConstants.KEY_CLASS_FREQUENCY, mBinding.classFrequency.getSelectedItem().toString());
        if (mBinding.maxStudentCapacity.getSelectedItemPosition() != 0) {
            user.put(AppConstants.KEY_MAX_STUDENTS, mBinding.maxStudentCapacity.getSelectedItem().toString());
        }
        user.put(AppConstants.KEY_RATE, mBinding.edtRate.getText().toString());
        user.put(AppConstants.KEY_PAYMENT_DURATION, mBinding.paymentDuration.getSelectedItem().toString());

        final Map<String, Object> user1 = new HashMap<>();
        user1.put(AppConstants.KEY_PHONE_NUMBER, getAppPreferenceHelper().getUserPhone());
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
                                        finishAffinity();
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

    private boolean isValid() {
        if (mBinding.classType.getSelectedItemPosition() == 0) {
            showSnackError(R.string.select_class_type);
            return false;
        }

        if (mBinding.numbOfClasses.getSelectedItemPosition() == 0) {
            showSnackError(R.string.select_number_of_class);
            return false;
        }

        if (mBinding.classFrequency.getSelectedItemPosition() == 0) {
            showSnackError(R.string.select_class_freq);
            return false;
        }

        if (mBinding.maxStudentCapacity.getVisibility() == View.VISIBLE && mBinding.maxStudentCapacity.getSelectedItemPosition() == 0) {
            showSnackError(R.string.select_max_students_capacity);
            return false;
        }

        if (TextUtils.isEmpty(getString(mBinding.edtRate.getText()))) {
            mBinding.edtRate.requestFocus();
            showSnackError(R.string.input_rate);
            return false;
        }
        return true;
    }

//    @OnClick(R.id.tvClassType)
//    public void onClassTypeClick(View v) {
//        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
//        tvClassType.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//        PopupWindow popUp = createPopupClassType();
//        popUp.showAsDropDown(v, 0, 10);
//    }
//
//    private PopupWindow createPopupClassType() {
//        popupClassType = new PopupWindow(this);
//        popupClassType.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items, classTypes);
//        ListView listViewSort = new ListView(this);
//        listViewSort.setDivider(null);
//        listViewSort.setAdapter(adapter);
//        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvClassType.setText(mSelectedClassType = classTypes[position]);
//                tvClassType.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//
//                if (mSelectedClassType.equals(classTypes[0])) {
//                    if (tvMaxStudents.getVisibility() == View.GONE) {
//                        tvMaxStudents.setVisibility(View.VISIBLE);
//                        tvMaxStudents.setText(R.string.specify_max_students);
//                    }
//                } else {
//                    tvMaxStudents.setVisibility(View.GONE);
//                    mSelectedMaxStudents = -1;
//                }
//
//                if (popupClassType != null) {
//                    popupClassType.dismiss();
//                }
//            }
//        });
//        setPopWidth(popupClassType);
//        popupClassType.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvClassType.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//            }
//        });
//        popupClassType.setFocusable(true);
//        popupClassType.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupClassType.setContentView(listViewSort);
//        return popupClassType;
//    }
//
//    @OnClick(R.id.tvClassNumber)
//    public void onClassNumberClick(View v) {
//        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
//        tvClassNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//        PopupWindow popUp = createPopupClassNumber();
//        popUp.showAsDropDown(v, 0, 10);
//    }
//
//    private PopupWindow createPopupClassNumber() {
//        popupClassNumber = new PopupWindow(this);
//        popupClassNumber.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, classNumbers);
//        ListView listViewSort = new ListView(this);
//        listViewSort.setDivider(null);
//        listViewSort.setAdapter(adapter);
//        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvClassNumber.setText("" + (mSelectedClassNumber = Integer.parseInt(classNumbers[position])));
//                tvClassNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//
//                if (popupClassNumber != null) {
//                    popupClassNumber.dismiss();
//                }
//            }
//        });
//        setPopWidth(popupClassNumber);
//        popupClassNumber.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvClassNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//            }
//        });
//        popupClassNumber.setFocusable(true);
//        popupClassNumber.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupClassNumber.setContentView(listViewSort);
//        return popupClassNumber;
//    }
//
//    @OnClick(R.id.tvClassFreq)
//    public void onClassFreqClick(View v) {
//        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
//        tvClassFreq.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//        PopupWindow popUp = createPopupClassFreq();
//        popUp.showAsDropDown(v, 0, 10);
//    }
//
//    private PopupWindow createPopupClassFreq() {
//        popupClassFreq = new PopupWindow(this);
//        popupClassFreq.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, classFreqs);
//        ListView listViewSort = new ListView(this);
//        listViewSort.setDivider(null);
//        listViewSort.setAdapter(adapter);
//        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvClassFreq.setText(mSelectedClassFreq = classFreqs[position]);
//                tvClassFreq.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//
//                if (popupClassFreq != null) {
//                    popupClassFreq.dismiss();
//                }
//            }
//        });
//        setPopWidth(popupClassFreq);
//        popupClassFreq.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvClassFreq.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//            }
//        });
//        popupClassFreq.setFocusable(true);
//        popupClassFreq.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupClassFreq.setContentView(listViewSort);
//        return popupClassFreq;
//    }
//
//    @OnClick(R.id.tvMaxStudents)
//    public void onMaxStudentsClick(View v) {
//        Drawable img = this.getResources().getDrawable(R.drawable.drop_down_arrow_up);
//        tvMaxStudents.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//        PopupWindow popUp = createPopupMaxStudents();
//        popUp.showAsDropDown(v, 0, 10);
//    }
//
//    private PopupWindow createPopupMaxStudents() {
//        popupMaxStudents = new PopupWindow(this);
//        popupMaxStudents.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dropdown_items, maxStudents);
//        ListView listViewSort = new ListView(this);
//        listViewSort.setDivider(null);
//        listViewSort.setAdapter(adapter);
//        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvMaxStudents.setText("" + (mSelectedMaxStudents = Integer.parseInt(maxStudents[position])));
//                tvMaxStudents.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//
//                if (popupMaxStudents != null) {
//                    popupMaxStudents.dismiss();
//                }
//            }
//        });
//        setPopWidth(popupMaxStudents);
//        popupMaxStudents.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                Drawable img = getDrawable(R.drawable.drop_down_arrow);
//                tvMaxStudents.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
//            }
//        });
//        popupMaxStudents.setFocusable(true);
//        popupMaxStudents.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupMaxStudents.setContentView(listViewSort);
//        return popupMaxStudents;
//    }
//
//    @OnClick(R.id.btnNext)
//    public void onNextClick() {
//        if (TextUtils.isEmpty(mSelectedClassType)) {
//            showSnackError(R.string.select_class_type);
//            return;
//        }
//
//        if (mSelectedClassNumber == -1) {
//            showSnackError(R.string.select_number_of_class);
//            return;
//        }
//
//        if (TextUtils.isEmpty(mSelectedClassFreq)) {
//            showSnackError(R.string.select_class_freq);
//            return;
//        }
//
//        if (mSelectedMaxStudents == -1 && tvMaxStudents.getVisibility() == View.VISIBLE) {
//            showSnackError(R.string.select_max_students_capacity);
//            return;
//        }
//
//        if (TextUtils.isEmpty(edtRate.getText().toString())) {
//            showSnackError(R.string.input_rate);
//            return;
//        }
//
//        showLoading();
//
//        final Map<String, Object> user = new HashMap<>();
//        user.put(KEY_CLASS_TYPE, mSelectedClassType);
//        user.put(KEY_NO_OF_CLASSES, mSelectedClassNumber);
//        user.put(KEY_CLASS_FREQUENCY, mSelectedClassFreq);
//        user.put(KEY_MAX_STUDENTS, mSelectedMaxStudents);
//        user.put(KEY_RATE, edtRate.getText().toString());
//
//        final Map<String, Object> user1 = new HashMap<>();
//        user1.put(KEY_PHONE_NUMBER, getAppPreferenceHelper().getUserPhone());
//
//        getFirebaseStore().collection(getString(R.string.db_root_tutors)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        getFirebaseStore().collection(getString(R.string.db_root_all_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user1, SetOptions.merge())
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        hideLoading();
//
//                                        Intent i = new Intent(TutorCreatePackage.this, TutorHomeActivity.class);
//                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        hideLoading();
//                                        showSnackError(e.getMessage());
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        hideLoading();
//                        showSnackError(e.getMessage());
//                    }
//                });
//    }
//
//    private void setPopWidth(PopupWindow popupWindow) {
//        if (width >= 1440) {
//            popupWindow.setWidth(width - 200);
//        } else if (width >= 1080) {
//            popupWindow.setWidth(width - 150);
//        } else if (width >= 720) {
//            popupWindow.setWidth(width - 100);
//        } else {
//            popupWindow.setWidth(width - 100);
//        }
//    }
//
//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }


    private static class SpinnerAdapter extends BaseAdapter {
        private final List<String> mData = new ArrayList<>();
        private final LayoutInflater mLayoutInflater;
        @ColorRes
        private int mTextColor = R.color.black;

        SpinnerAdapter(Context context, String[] data) {
            mLayoutInflater = LayoutInflater.from(context);
            mData.addAll(Arrays.asList(data));
        }

        SpinnerAdapter(Context context, String[] data, @ColorRes int color) {
            mLayoutInflater = LayoutInflater.from(context);
            mData.addAll(Arrays.asList(data));
            mTextColor = color;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_spinner, parent, false);
            }
            TextView text = convertView.findViewById(R.id.text);
            text.setText(mData.get(position));
            text.setTextColor(ContextCompat.getColor(mLayoutInflater.getContext(), mTextColor));
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_spinner_drop_down, parent, false);
            }
            TextView text = convertView.findViewById(R.id.text);
            text.setTextColor(ContextCompat.getColor(mLayoutInflater.getContext(), mTextColor));
            text.setText(mData.get(position));
            return convertView;
        }
    }
}
