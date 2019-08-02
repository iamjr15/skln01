package com.autohub.skln;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.autohub.skln.utills.AppConstants;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-07-30.
 */
public class FrameActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        String fragmentName = getIntent().getStringExtra(AppConstants.KEY_FRAGMENT);
        if (TextUtils.isEmpty(fragmentName)) {
            throw new IllegalArgumentException("Fragment Name must not be null");
        }
        Bundle bundle = getIntent().getBundleExtra(AppConstants.KEY_DATA);

        if (savedInstanceState == null) {
            Fragment fragment = Fragment.instantiate(this, fragmentName);
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment).commit();
        }
    }
}
