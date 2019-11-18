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

    Boolean getStudentSignUpComplete();

    void setStudentSignupComplete(boolean isSignUpComplete);

    Boolean getTutorSignUpComplete();

    void setTutorSignUpComplete(boolean isSignUpComplete);

    String getuserID();

    void setUserId(String userID);



//    String getTutorName();
}