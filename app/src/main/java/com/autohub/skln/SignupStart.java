package com.autohub.skln;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.autohub.skln.databinding.TutorSignupStartBinding;
import com.autohub.skln.listeners.AddressListener;
import com.autohub.skln.student.StudentClassSelect;
import com.autohub.skln.tutor.TutorCategorySelect;
import com.autohub.skln.utills.ActivityUtils;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.GpsUtils;
import com.autohub.skln.utills.LocationProvider;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.SetOptions;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.autohub.skln.utills.AppConstants.FEMALE;
import static com.autohub.skln.utills.AppConstants.KEY_ACCOUNT_TYPE;
import static com.autohub.skln.utills.AppConstants.KEY_CITY;
import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_LATITUDE;
import static com.autohub.skln.utills.AppConstants.KEY_LONGITUDE;
import static com.autohub.skln.utills.AppConstants.KEY_PASSWORD;
import static com.autohub.skln.utills.AppConstants.KEY_PHONE_NUMBER;
import static com.autohub.skln.utills.AppConstants.KEY_SEX;
import static com.autohub.skln.utills.AppConstants.MALE;
import static com.autohub.skln.utills.AppConstants.TYPE_TUTOR;

public class SignupStart extends BaseActivity {
    private TutorSignupStartBinding mBinding;
    private String mType = TYPE_TUTOR;
    private String phoneNumber;
    private GpsUtils mGpsUtils;
    private String mCity;
    private Location mLocation;
    private final LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(">>>>Location", location.toString());
            mLocation = location;
            LocationProvider.getInstance().getAddressFromLocation(SignupStart.this, location, new AddressListener() {
                @Override
                public void onAddressDecoded(String address) {
                    Log.d(">>>>LocationAddress", "Address is :" + address);
                    mCity = address;
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.tutor_signup_start);
        mBinding.setCallback(this);
        mBinding.tvTutorOrStudent.setText(R.string.student);
        mType = getIntent().getStringExtra(KEY_ACCOUNT_TYPE);
        if (mType.equalsIgnoreCase(TYPE_TUTOR)) {
            mBinding.tvTutorOrStudent.setText(R.string.teacher);
        }
        phoneNumber = getIntent().getStringExtra(AppConstants.KEY_PHONE_NUMBER);
        mGpsUtils = new GpsUtils(this);

        if (!mGpsUtils.isGpsEnabled()) {
            mGpsUtils.turnGPSOn(new GpsUtils.OnGpsListener() {
                @Override
                public void onGpsStatus(boolean isGPSEnable) {
                    Log.d(">>>>Location", "enabled");
                }
            });
        }
    }

    public void onNextClick() {
        if (isValid(mBinding.edtFirstName, mBinding.edtLastName)) {
            Editable password = mBinding.edtPassword.getText();
            if (password == null || password.length() == 0) {
                mBinding.edtPassword.setError(getResources().getString(R.string.enter_password));
                mBinding.edtPassword.requestFocus();
                showSnackError(R.string.enter_password);
                return;
            }

            if (mLocation == null || TextUtils.isEmpty(mCity)) {
                Toast.makeText(this, "Please check you GPS setting, we need You location", Toast.LENGTH_SHORT).show();
                return;
            }
            makeSaveRequest();
        }
    }

    private void makeSaveRequest() {
        showLoading();
        final Map<String, Object> user = new HashMap<>();
        user.put(KEY_FIRST_NAME, mBinding.edtFirstName.getText().toString());
        user.put(KEY_LAST_NAME, mBinding.edtLastName.getText().toString());
        user.put(KEY_SEX, mBinding.radioMale.isChecked() ? MALE : FEMALE);
        user.put(KEY_PASSWORD, getEncryptedPassword());
        user.put(KEY_PHONE_NUMBER, phoneNumber);
        user.put(KEY_CITY, mCity);
        user.put(KEY_LATITUDE, mLocation.getLatitude());
        user.put(KEY_LONGITUDE, mLocation.getLongitude());
        String dbRoot = getString(R.string.db_root_students);
        if (TYPE_TUTOR.equalsIgnoreCase(mType)) {
            dbRoot = getString(R.string.db_root_tutors);
        }

        getFirebaseStore().collection(dbRoot).document(getFirebaseAuth().getCurrentUser().getUid()).set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        hideLoading();
                        ActivityUtils.launchActivity(SignupStart.this,
                                mType.equalsIgnoreCase(TYPE_TUTOR) ?
                                        TutorCategorySelect.class : StudentClassSelect.class);
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

    @NonNull
    private String getEncryptedPassword() {
        try {
            return encrypt(getString(mBinding.edtPassword.getText()));
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
        return getString(mBinding.edtPassword.getText());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkGooglePlayServices() && isLocationPermissionGranted()) {
            Log.d(">>>>Location", "Oncreate");
            LocationProvider.getInstance().start(this, mLocationListener);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 2321);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationProvider.getInstance().stopLocationUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2321) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkGooglePlayServices() && isLocationPermissionGranted()) {
                    LocationProvider.getInstance().start(this, mLocationListener);
                }
            }
        }
    }
}
