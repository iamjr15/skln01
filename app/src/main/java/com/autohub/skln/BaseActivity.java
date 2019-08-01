package com.autohub.skln;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.autohub.skln.pref.PreferencesImpl;
import com.autohub.skln.utills.AppConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class BaseActivity extends AppCompatActivity {

    protected static final int FONT_TYPE_MONTSERRAT_BOLD = 0;
    protected static final int FONT_TYPE_CERAPRO_BOLD = 1;
    protected static final int FONT_TYPE_MONTSERRAT_REGULAR = 2;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseFirestore mFirebaseFirestore;

    private PreferencesImpl mPreferencesImpl;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mPreferencesImpl = new PreferencesImpl(this, AppConstants.PREF_NAME);
    }

    protected FirebaseAuth getFirebaseAuth() {
        return mFirebaseAuth;
    }

    protected FirebaseFirestore getFirebaseStore() {
        return mFirebaseFirestore;
    }

    protected PreferencesImpl getAppPreferenceHelper() {
        return mPreferencesImpl;
    }

    /*
    t@ngel : show loading progress
     */
    protected void showLoading() {
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
    protected void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    /*
    t@ngel : show error message using snack bar
    param : message to be shown as a String resource id
     */
    public void showSnackError(int resId) {
        Snackbar snackbar = Snackbar.make((findViewById(android.R.id.content)), getString(resId), Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snack_back_color));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /*
    t@ngel : show error message using snack bar
    param : message as a String
     */
    public void showSnackError(String message) {
        Snackbar snackbar = Snackbar.make((findViewById(android.R.id.content)), message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snack_back_color));
        TextView textView = snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /*
    t@ngel : encrypt password
     */
    protected static String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AppConstants.KEY.getBytes(), AppConstants.ALGORITHM);
        Cipher cipher = Cipher.getInstance(AppConstants.MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(AppConstants.IV.getBytes()));
        byte[] values = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(values, Base64.DEFAULT);
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

    @NonNull
    protected String getString(@Nullable Editable text) {
        return (text == null || text.length() == 0) ? "" : text.toString();
    }

    protected boolean isValid(@NonNull EditText... editTexts) {
        for (EditText editText : editTexts) {
            Editable text = editText.getText();
            if (text == null || text.length() < 2) {
                CharSequence hint = editText.getHint();
                if (TextUtils.isEmpty(hint)) {
                    hint = "valid value";
                }
                editText.setError(getString(R.string.enter_x, hint));
                editText.requestFocus();
                return false;
            }
        }
        return true;
    }
}
