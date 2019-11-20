package com.autohub.skln;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.autohub.skln.pref.PreferencesImpl;
import com.autohub.skln.utills.AppConstants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 11332;
    private static final int FONT_TYPE_MONTSERRAT_BOLD = 0;
    protected static final int FONT_TYPE_CERAPRO_BOLD = 1;
    private static final int FONT_TYPE_MONTSERRAT_REGULAR = 2;

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
    t@ngel : show error message using snack bar
    param : message to be shown as a String resource id
     */
    public void showSnackError(int resId) {
        Snackbar snackbar = Snackbar.make((findViewById(android.R.id.content)), getString(resId), Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.snack_back_color));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
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
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /*
    t@ngel : encrypt password
     */
    public static String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
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

    public void setStatusBarColor(@DrawableRes int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Drawable background = getResources().getDrawable(drawable);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.transparent));
            // window.setNavigationBarColor(requireActivity().getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    protected boolean checkGooglePlayServices() {

        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            /*
             * google play services is missing or update is required
             *  return code could be
             * SUCCESS,
             * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
             * SERVICE_DISABLED, SERVICE_INVALID.
             */
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return true;
        }

        return false;

    }

    protected boolean isLocationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    protected boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
