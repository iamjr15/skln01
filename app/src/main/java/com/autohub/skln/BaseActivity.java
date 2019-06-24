package com.autohub.skln;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.autohub.skln.pref.PreferencesImpl;
import com.autohub.skln.utills.AppConstants;

public class BaseActivity extends AppCompatActivity {

    protected static final int FONT_TYPE_MONTSERRAT_BOLD = 0;
    protected static final int FONT_TYPE_CERAPRO_BOLD = 1;
    protected static final int FONT_TYPE_MONTSERRAT_REGULAR = 2;


    private FirebaseFirestore mFirebaseFirestore;
    private PreferencesImpl mPreferencesImpl;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mPreferencesImpl = new PreferencesImpl(this, AppConstants.PREF_NAME);
    }

    public FirebaseFirestore getFirebaseStore() {
        return mFirebaseFirestore;
    }

    public PreferencesImpl getAppPreferenceHelper() {
        return mPreferencesImpl;
    }

    /*
    t@ngel : show loading progress
     */
    public void showLoading() {
        try {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please wait...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    t@ngel : hide progress dialog
     */
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    /*
    t@ngel : set custom typeface
     */
    protected void setCustomTypeface(TextView v, int type) {
        if (v == null)
            return;

        Typeface typeface = null;
        if (type == FONT_TYPE_MONTSERRAT_BOLD) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        } else if (type == FONT_TYPE_CERAPRO_BOLD) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Bold.ttf");
        } else if (type == FONT_TYPE_MONTSERRAT_REGULAR) {
            typeface = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.otf");
        }

        if (typeface != null)
            v.setTypeface(typeface);
    }
}
