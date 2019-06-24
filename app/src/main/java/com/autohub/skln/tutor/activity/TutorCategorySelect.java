package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TutorCategorySelect extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_category_select);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rlAcademics)
    public void onAcademicClick()
    {
        getAppPreferenceHelper().setTutorCategory(getString(R.string.academics));
        startActivity(new Intent(this, TutorClassSelect.class));
    }

    @OnClick(R.id.rlHobby)
    public void onHobbyClick() {
        getAppPreferenceHelper().setTutorCategory(getString(R.string.hobby));
        startActivity(new Intent(this, TutorHobbySelect.class));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
