package com.autohub.skln.pref;


/**
 *Modified by Vt Netzwelt
 */

public interface PreferencesHelper {

    void setUserPhoneNumber(String phone);

    void setTutorCategory(String category);

//    void setTutorName(String fname);

    String getUserPhone();

    String getTutorCategory();

    Boolean getSignUpComplete();

    void setSignupComplete(boolean isSignUpComplete);

    String getuserID();

    void setUserId(String userID);



//    String getTutorName();
}