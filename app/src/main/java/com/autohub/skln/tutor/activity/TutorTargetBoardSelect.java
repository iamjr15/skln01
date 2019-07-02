package com.autohub.skln.tutor.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import static com.autohub.skln.utills.AppConstants.BOARD_CBSE;
import static com.autohub.skln.utills.AppConstants.BOARD_ICSE;
import static com.autohub.skln.utills.AppConstants.BOARD_STATE;
import static com.autohub.skln.utills.AppConstants.KEY_AREA_QUALIFICATION;
import static com.autohub.skln.utills.AppConstants.KEY_BOARD;
import static com.autohub.skln.utills.AppConstants.KEY_QUALIFICATION;

public class TutorTargetBoardSelect extends BaseActivity {

    @BindView(R.id.tvSelectQualification)
    TextView tvSelectQualification;

    @BindView(R.id.tvSelectAreaQualification)
    TextView tvSelectAreaQualification;

    @BindView(R.id.radioCBSE)
    RadioButton radioCBSE;

    @BindView(R.id.radioICSE)
    RadioButton radioICSE;

    @BindView(R.id.radioState)
    RadioButton radioState;

    @BindView(R.id.board_group)
    RadioGroup radioGroupBoards;

    private String[] qualifications;
    private String[] areasQualifies;

    private String mSelectedTargetBoard = "";
    private String mSelectedQualification = "";
    private String mSelectedArea = "";

    private PopupWindow popupWindowQualification;
    private PopupWindow popupWindowArea;

    private int width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_target_board);
        ButterKnife.bind(this);

        qualifications = getResources().getStringArray(R.array.qualification_arrays);
//        areasQualifies = getResources().getStringArray(R.array.area_qualifi_arrays);
    }

    @OnClick(R.id.btnNext)
    public void onNextClick() {
        if (TextUtils.isEmpty(mSelectedTargetBoard)) {
            showSnackError(R.string.select_board_message);
            return;
        }

        if (TextUtils.isEmpty(mSelectedQualification)) {
            showSnackError(R.string.select_qualification);
            return;
        }

        if (TextUtils.isEmpty(mSelectedArea)) {
            showSnackError(R.string.select_area_qualification);
            return;
        }

//        getAppPreferenceHelper().setTutorTargetedBoard(mSelectedTargetBoard, tvSelectQualification.getText().toString(),
//                tvSelectAreaQualification.getText().toString());
//
//        startActivity(new Intent(this, TutorBiodata.class));

        showLoading();

        Map<String, Object> user = new HashMap<>();
        user.put(KEY_BOARD, mSelectedTargetBoard);
        user.put(KEY_QUALIFICATION, mSelectedQualification);
        user.put(KEY_AREA_QUALIFICATION, mSelectedArea);

        getFirebaseStore().collection(getString(R.string.db_root_users)).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        startActivity(new Intent(TutorTargetBoardSelect.this, TutorBiodata.class));
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

    @OnClick(R.id.rlCBSE)
    public void onRlCBSEClick() {
        radioGroupBoards.check(R.id.radioCBSE);
    }

    @OnClick(R.id.rlICSE)
    public void onRlICSEClick() {
        radioGroupBoards.check(R.id.radioICSE);
    }

    @OnClick(R.id.rlSTATE)
    public void onRlStateClick() {
        radioGroupBoards.check(R.id.radioState);
    }

    @OnClick(R.id.radioCBSE)
    public void onCBSEClick() {
        mSelectedTargetBoard = BOARD_CBSE;
    }

    @OnClick(R.id.radioICSE)
    public void onICSEClick() {
        mSelectedTargetBoard = BOARD_ICSE;
    }

    @OnClick(R.id.radioState)
    public void onStateClick() {
        mSelectedTargetBoard = BOARD_STATE;
        /*tvCBSE.setBackgroundResource(R.drawable.reactangle_cert);
        tvCBSE.setTextColor(Color.parseColor("#000000"));
        radioICSE.setBackgroundResource(R.drawable.reactangle_cert);
        radioICSE.setTextColor(Color.parseColor("#000000"));
        radioState.setBackgroundResource(R.drawable.reactangle_cert_black);
        radioState.setTextColor(Color.parseColor("#ffffff"));*/
    }

    @OnClick(R.id.tvSelectQualification)
    public void onQualificationClick(View v) {
        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        img.setBounds(0, 0, 120, 120);
        tvSelectQualification.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = popupWindowQualification();
        popUp.showAsDropDown(v, 0, 10);
    }

    @OnClick(R.id.tvSelectAreaQualification)
    public void onAreaClick(View v) {
        if (TextUtils.isEmpty(mSelectedQualification) || mSelectedQualification.equals(qualifications[2])) {
            showSnackError(R.string.select_qualification_first);
            return;
        }

        Drawable img = this.getResources().getDrawable(R.drawable.chevron_with_circle_up);
        img.setBounds(0, 0, 120, 120);
        tvSelectAreaQualification.setCompoundDrawables(null, null, img, null);
        PopupWindow popUp = popupWindowArea();
        popUp.showAsDropDown(v, 0, 10);
    }

    private PopupWindow popupWindowQualification() {
        popupWindowQualification = new PopupWindow(this);
        popupWindowQualification.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items,
                getResources().getStringArray(R.array.qualification_arrays));
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                img.setBounds(0, 0, 120, 120);
                tvSelectQualification.setText(mSelectedQualification = qualifications[position]);
                tvSelectQualification.setCompoundDrawables(null, null, img, null);

                if (mSelectedQualification.equals(qualifications[0]))
                    areasQualifies = getResources().getStringArray(R.array.area_qualifi_arrays_1);
                else if (mSelectedQualification.equals(qualifications[1]))
                    areasQualifies = getResources().getStringArray(R.array.area_qualifi_arrays_2);
                else if (mSelectedQualification.equals(qualifications[2])) {
                    tvSelectAreaQualification.setText(mSelectedArea = "N/A");
                }

                if (popupWindowQualification != null) {
                    popupWindowQualification.dismiss();
                }
            }
        });
        setPopWidth(popupWindowQualification);
        popupWindowQualification.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                img.setBounds(0, 0, 120, 120);
                tvSelectQualification.setCompoundDrawables(null, null, img, null);

            }
        });
        popupWindowQualification.setFocusable(true);
        popupWindowQualification.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowQualification.setContentView(listViewSort);

        return popupWindowQualification;
    }

    // initialize a pop up window type
    private PopupWindow popupWindowArea() {
        popupWindowArea = new PopupWindow(this);
        popupWindowArea.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.reactangle_cert));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_items, areasQualifies);
        ListView listViewSort = new ListView(this);
        listViewSort.setDivider(null);
        listViewSort.setAdapter(adapter);
        listViewSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                img.setBounds(0, 0, 120, 120);
                tvSelectAreaQualification.setText(areasQualifies[position]);
                tvSelectAreaQualification.setCompoundDrawables(null, null, img, null);

                if (popupWindowArea != null) {
                    popupWindowArea.dismiss();
                }
            }
        });
        setPopWidth(popupWindowArea);
        popupWindowArea.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDismiss() {
                Drawable img = getDrawable(R.drawable.chevron_with_circle_down);
                img.setBounds(0, 0, 120, 120);
                tvSelectAreaQualification.setCompoundDrawables(null, null, img, null);

            }
        });
        popupWindowArea.setFocusable(true);
        popupWindowArea.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindowArea.setContentView(listViewSort);

        return popupWindowArea;
    }

    private void setPopWidth(PopupWindow popupWindow) {
        if (width == 1440) {
            popupWindow.setWidth(width - 200);
        } else if (width == 1080) {
            popupWindow.setWidth(width - 150);
        } else if (width == 720) {
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
