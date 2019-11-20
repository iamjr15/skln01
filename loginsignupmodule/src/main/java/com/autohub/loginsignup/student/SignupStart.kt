package com.autohub.loginsignup.student

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.autohub.loginsignup.R
import com.autohub.loginsignup.databinding.TutorSignupStartBinding
import com.autohub.loginsignup.utility.Utilities
import com.autohub.skln.BaseActivity
import com.autohub.skln.utills.ActivityUtils
import com.autohub.skln.utills.AppConstants.*
import com.autohub.skln.utills.GpsUtils
import com.autohub.skln.utills.LocationProvider
import com.google.android.gms.location.LocationListener

/**
 * Created by Vt Netzwelt
 */
class SignupStart : BaseActivity() {
    private var mBinding: TutorSignupStartBinding? = null
    private var mCity: String? = null
    private var mLocation: Location? = null
    private val mLocationListener = LocationListener { location ->
        mLocation = location
        LocationProvider.getInstance().getAddressFromLocation(this@SignupStart, location) { address ->
            Log.d(">>>>LocationAddress", "Address is :$address")
            mCity = address
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.tutor_signup_start)
        mBinding!!.callback = this

        val mGpsUtils = GpsUtils(this)

        if (!mGpsUtils.isGpsEnabled) {
            mGpsUtils.turnGPSOn { Log.d(">>>>Location", "enabled") }
        }

        Utilities.animateProgressbar(mBinding!!.pbSignupProgress, 0.0f, 20.0f)

        if (!checkGooglePlayServices() && !isLocationPermissionGranted) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 2321)

        }

    }

    fun onNextClick() {
        if (isValid(mBinding!!.edtFirstName, mBinding!!.edtLastName)) {
            val password = mBinding!!.edtPassword.text
            val email = mBinding!!.edtemail.text
            if (email == null) {
                mBinding!!.edtemail.error = resources.getString(R.string.enter_email)
                mBinding!!.edtemail.requestFocus()
                return
            }

            if (!isValidEmailId(email.toString())) {
                mBinding!!.edtemail.error = resources.getString(R.string.enter_validemail)
                mBinding!!.edtemail.requestFocus()
                return
            }

            if (password == null || password.isEmpty()) {
                mBinding!!.edtPassword.error = resources.getString(R.string.enter_password)
                mBinding!!.edtPassword.requestFocus()
//                showSnackError(R.string.enter_password)
                return
            }

            if (password.toString().length < 7) {
                mBinding!!.edtPassword.error = getString(R.string.passwordweak)
                mBinding!!.edtPassword.requestFocus()
//                showSnackError(R.string.enter_password)
                return
            }


            if (mLocation == null || TextUtils.isEmpty(mCity)) {
                Toast.makeText(this, "Please check you GPS setting, we need You location", Toast.LENGTH_SHORT).show()
                return
            }

            makeSaveRequest()
        }

    }


    private fun makeSaveRequest() {
        val userMap = HashMap<String, Any>()
        userMap[KEY_FIRST_NAME] = mBinding!!.edtFirstName.text.toString()
        userMap[KEY_LAST_NAME] = mBinding!!.edtLastName.text.toString()
        userMap[KEY_EMAIL] = mBinding!!.edtemail.text.toString()
        userMap[KEY_SEX] = if (mBinding!!.radioMale.isChecked) MALE else FEMALE
        userMap[KEY_PASSWORD] = getString(mBinding!!.edtPassword.text)
        userMap[KEY_CITY] = mCity!!
        userMap[KEY_LONGITUDE] = mLocation!!.longitude
        userMap[KEY_LATITUDE] = mLocation!!.latitude


//        userMap[KEY_LONGITUDE] = mLocation!!.longitude
        val extras = Bundle()
        extras.putSerializable(KEY_USERMAP, userMap)
        ActivityUtils.launchActivity(this@SignupStart, NumberVerificationActivity::class.java, extras)
    }


    override fun onResume() {
        super.onResume()
        if (!checkGooglePlayServices() && isLocationPermissionGranted) {
            Log.d(">>>>Location", "Oncreate")
            LocationProvider.getInstance().start(this, mLocationListener)
        }
    }

    override fun onPause() {
        super.onPause()
        LocationProvider.getInstance().stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2321) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!checkGooglePlayServices() && isLocationPermissionGranted) {
                    LocationProvider.getInstance().start(this, mLocationListener)
                }
            }
        }
    }
}
