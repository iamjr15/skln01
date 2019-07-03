package com.autohub.skln.pref;


public interface PreferencesHelper {

    void setUserPhoneNumber(String phone);

    void setTutorCategory(String category);

    void setTutorName(String fname);

    String getUserPhone();

    String getTutorCategory();

    String getTutorName();
}