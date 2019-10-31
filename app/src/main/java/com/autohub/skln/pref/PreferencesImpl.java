package com.autohub.skln.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesImpl implements PreferencesHelper {

    private final SharedPreferences mPrefs;

    private static final String PREF_KEY_USER_PHONE = "PREF_KEY_USER_PHONE";
    //    private static final String PREF_KEY_TUTOR_NAME = "PREF_KEY_USER_FNAME";
    private static final String PREF_KEY_TUTOR_CATEGORY = "PREF_KEY_TUTOR_CATEGORY";
    private static final String PREF_KEY_STUDENT_SIGNUP_COMPLETE = "PREF_KEY_STUDENT_SIGNUP_COMPLETE";

    public PreferencesImpl(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getUserPhone() {
        return mPrefs.getString(PREF_KEY_USER_PHONE, null);
    }

    @Override
    public String getTutorCategory() {
        return mPrefs.getString(PREF_KEY_TUTOR_CATEGORY, null);
    }

    @Override
    public Boolean getSignUpComplete() {
        return mPrefs.getBoolean(PREF_KEY_STUDENT_SIGNUP_COMPLETE, false);
    }

    @Override
    public void setSignupComplete(boolean isSignUpComplete) {
        mPrefs.edit().putBoolean(PREF_KEY_STUDENT_SIGNUP_COMPLETE, isSignUpComplete).apply();


    }





    /*@Override
    public String getTutorName() {
        return mPrefs.getString(PREF_KEY_TUTOR_NAME, null);
    }*/

    @Override
    public void setUserPhoneNumber(String phone) {
        mPrefs.edit().putString(PREF_KEY_USER_PHONE, phone).apply();
    }

    @Override
    public void setTutorCategory(String category) {
        mPrefs.edit().putString(PREF_KEY_TUTOR_CATEGORY, category).apply();
    }



    /*@Override
    public void setTutorName(String first_name) {
        mPrefs.edit().putString(PREF_KEY_TUTOR_NAME, first_name).apply();
    }*/
}