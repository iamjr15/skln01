package com.autohub.skln.tutor.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.autohub.skln.BaseActivity;
import com.autohub.skln.R;
import com.autohub.skln.NumberVerificationActivity;
import com.autohub.skln.utills.AppConstants;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class TutorSignupStart extends BaseActivity {

    @BindView(R.id.edtFirstName)
    EditText edtFirstName;

    @BindView(R.id.edtLastName)
    EditText edtLastName;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_signup_start);
        ButterKnife.bind(this);
//        selectedTitle = getResources().getStringArray(R.array.title_arrays);
//        tvSelectTitle.setText(selectedTitle[0]);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
//        width = displayMetrics.widthPixels;

    }

    /*The below method is for to getting data from user and save to the shared preferences*/
    @OnClick(R.id.btnNext)
    public void onNextClick() {

      if (edtFirstName.getText().length() == 0) {

          snackbar =Snackbar.make((findViewById(android.R.id.content)), getString(R.string.enter_name), Snackbar.LENGTH_LONG);
          View view = snackbar.getView();
          TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
          view.setBackgroundColor(Color.parseColor("#ba0505"));
          textView.setTextColor(Color.WHITE);
          snackbar.show();

            return;
        }
        if (edtLastName.getText().length() == 0) {

            snackbar =Snackbar.make((findViewById(android.R.id.content)), getString(R.string.enter_lastname), Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.parseColor("#ba0505"));
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return;
        }

        if (edtPassword.getText().length() == 0) {
            snackbar =Snackbar.make((findViewById(android.R.id.content)), getString(R.string.enter_password), Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            view.setBackgroundColor(Color.parseColor("#ba0505"));
            textView.setTextColor(Color.WHITE);
            snackbar.show();
            return;
        }

        try {
            /*Save the data to the shared preferences*/
            getAppPreferenceHelper().setTutorMainInfo(edtFirstName.getText().toString(),
                    edtLastName.getText().toString(), encrypt(edtPassword.getText().toString()));
            /*redirect to the second activity*/
            startActivity(new Intent(this, TutorCategorySelect.class));
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

    }

    // Encryption Process
    public static String encrypt(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(AppConstants.KEY.getBytes(), AppConstants.ALGORITHM);
        Cipher cipher = Cipher.getInstance(AppConstants.MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(AppConstants.IV.getBytes()));
        byte[] values = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(values, Base64.DEFAULT);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
