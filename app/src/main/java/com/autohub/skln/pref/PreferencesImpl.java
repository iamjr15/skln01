package com.autohub.skln.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesImpl implements PreferencesHelper {

    private final SharedPreferences mPrefs;

//    private static final String PREF_KEY_USER_TITLE = "PREF_KEY_USER_TITLE";
    private static final String PREF_KEY_USER_FIRST_NAME = "PREF_KEY_USER_FIRST_NAME";
    private static final String PREF_KEY_USER_LAST_NAME = "PREF_KEY_USER_LAST_NAME";
    private static final String PREF_KEY_USER_PHONE = "PREF_KEY_USER_PHONE";
    private static final String PREF_KEY_USER_PASSWORD = "PREF_KEY_USER_PASSWORD";
    private static final String PREF_KEY_USER_ID = "PREF_KEY_USER_ID";
    private static final String PREF_KEY_OCCUPATION = "PREF_KEY_OCCUPATION";
    private static final String PREF_KEY_PROFILE_IMAGE = "PREF_KEY_PROFILE_IMAGE";
    private static final String PREF_KEY_CERTICATE_IMAGE = "PREF_KEY_CERTIFICATE_IMAGE";
    private static final String PREF_KEY_PROOF_DOCUMENT = "PREF_KEY_PROOF_DOCUMENT";
    private static final String PREF_KEY_PROFILE_IMAGE_FOR_HOBBIES = "PREF_KEY_OCCUPATION";
    private static final String PREF_KEY_CERTICATE_IMAGE_FOR_HOBBIES = "PREF_KEY_OCCUPATION";
    private static final String PREF_KEY_PROOF_DOCUMENT_FOR_HOBBIES = "PREF_KEY_OCCUPATION";
    private static final String PREF_KEY_EXPERINCE = "PREF_KEY_EXPERINCE";
    private static final String PREF_KEY_QUALIFICATION = "PREF_KEY_QUALIFICATION";
    private static final String PREF_KEY_AREA = "PREF_KEY_AREA";
    private static final String PREF_KEY_UNIVERSITY = "PREF_KEY_UNIVERSITY";
    private static final String PREF_KEY_SPECIALISATION = "PREF_KEY_SPECIALISATION";
    private static final String PREF_KEY_YEAR = "PREF_KEY_YEAR";
    private static final String PREF_KEY_OCCUPATION_FOR_HOBBIES = "PREF_KEY_OCCUPATION_FOR_HOBBIES";
    private static final String PREF_KEY_EXPERINCE_FOR_HOBBIES = "PREF_KEY_EXPERINCE_FOR_HOBBIES";
    private static final String PREF_KEY_QUALIFICATION_FOR_HOBBIES = "PREF_KEY_QUALIFICATION_FOR_HOBBIES";
    private static final String PREF_KEY_AREA_FOR_HOBBIES = "PREF_KEY_AREA_FOR_HOBBIES";
    private static final String PREF_KEY_UNIVERSITY_FOR_HOBBIES = "PREF_KEY_UNIVERSITY_FOR_HOBBIES";
    private static final String PREF_KEY_SPECIALISATION_FOR_HOBBIES = "PREF_KEY_SPECIALISATION_FOR_HOBBIES";
    private static final String PREF_KEY_YEAR_FOR_HOBBIES = "PREF_KEY_YEAR_FOR_HOBBIES";
    private static final String PREF_KEY_BOARD = "PREF_KEY_BOARD";
    private static final String PREF_KEY_EXPERINCE_LEVEL = "PREF_KEY_EXPERINCE_LEVEL";
    private static final String PREF_KEY_EXPERINCE_LEVEL_FOR_HOBBIES = "PREF_KEY_EXPERINCE_LEVEL_FOR_HOBBIES";
    private static final String PREF_KEY_WORKING_HOURS = "PREF_KEY_WORKING_HOURS";
    private static final String PREF_KEY_WORKING_HOURS_FOR_HOBBIES = "PREF_KEY_WORKING_HOURS_FOR_HOBBIES";
    private static final String PREF_KEY_BIODATA = "PREF_KEY_BIODATA";
    private static final String PREF_KEY_CLASSES_PLACE = "PREF_KEY_CLASSES_PLACE";
    private static final String PREF_KEY_CLASSES = "PREF_KEY_CLASSES";
    private static final String PREF_KEY_SUBJECTS = "PREF_KEY_SUBJECTS";
    private static final String PREF_KEY_TUTOR_CATEGORY = "PREF_KEY_TUTOR_CATEGORY";

    public PreferencesImpl(Context context, String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public String getUserFirstName() {
        return mPrefs.getString(PREF_KEY_USER_FIRST_NAME, null);
    }

    @Override
    public String getUserLastName() {
        return mPrefs.getString(PREF_KEY_USER_LAST_NAME, null);
    }

    @Override
    public String getUserPhone() {
        return mPrefs.getString(PREF_KEY_USER_PHONE, null);
    }

    @Override
    public String getUserPassword() {
        return mPrefs.getString(PREF_KEY_USER_PASSWORD, null);
    }

    @Override
    public String getUserId() {
        return mPrefs.getString(PREF_KEY_USER_ID, null);
    }

    @Override
    public String getOccupation() {
        return mPrefs.getString(PREF_KEY_OCCUPATION, null);
    }

    @Override
    public String getExperince() {
        return mPrefs.getString(PREF_KEY_EXPERINCE, null);
    }

    @Override
    public String getQualification() {
        return mPrefs.getString(PREF_KEY_QUALIFICATION, null);
    }

    @Override
    public String getArea() {
        return mPrefs.getString(PREF_KEY_AREA, null);
    }

    @Override
    public String getUniversity() {
        return mPrefs.getString(PREF_KEY_UNIVERSITY, null);
    }

    @Override
    public String getSpecialisation() {
        return mPrefs.getString(PREF_KEY_SPECIALISATION, null);
    }

    @Override
    public String getYear() {
        return mPrefs.getString(PREF_KEY_YEAR, null);
    }

    @Override
    public String getboard() {
        return mPrefs.getString(PREF_KEY_BOARD, null);
    }

    @Override
    public String getexperinceLevel() {
        return mPrefs.getString(PREF_KEY_EXPERINCE_LEVEL, null);
    }

    @Override
    public String getworkingHour() {
        return mPrefs.getString(PREF_KEY_WORKING_HOURS, null);
    }

    @Override
    public String getOverview() {
        return mPrefs.getString(PREF_KEY_BIODATA, null);
    }

    @Override
    public String getClassesPlace() {
        return mPrefs.getString(PREF_KEY_CLASSES_PLACE, null);
    }

    @Override
    public String getClasses() {
        return mPrefs.getString(PREF_KEY_CLASSES, null);
    }

    @Override
    public String getSubjects() {
        return mPrefs.getString(PREF_KEY_SUBJECTS, null);
    }

    @Override
    public String getProfileImage() {
        return mPrefs.getString(PREF_KEY_PROFILE_IMAGE, null);
    }

    @Override
    public String getCertificatesImage() {
        return mPrefs.getString(PREF_KEY_CERTICATE_IMAGE, null);
    }

    @Override
    public String getProofImage() {
        return mPrefs.getString(PREF_KEY_PROOF_DOCUMENT, null);
    }

    public String getTutorCategory() { return mPrefs.getString(PREF_KEY_TUTOR_CATEGORY, null); }

    @Override
    public void setTutorPrimaryInfo(String firstName, String lastName, String password) {
        mPrefs.edit().putString(PREF_KEY_USER_FIRST_NAME, firstName).apply();
        mPrefs.edit().putString(PREF_KEY_USER_LAST_NAME, lastName).apply();
        mPrefs.edit().putString(PREF_KEY_USER_PASSWORD, password).apply();
    }

    @Override
    public void setUserId(String id) {
        mPrefs.edit().putString(PREF_KEY_USER_ID, id).apply();
    }

    @Override
    public void setUserPhoneNumber(String phone) {
        mPrefs.edit().putString(PREF_KEY_USER_PHONE, phone).apply();
    }

    @Override
    public void setTutorPicture(String profileImage, String occupation, String experince) {
        mPrefs.edit().putString(PREF_KEY_PROFILE_IMAGE, profileImage).apply();
        mPrefs.edit().putString(PREF_KEY_OCCUPATION, occupation).apply();
        mPrefs.edit().putString(PREF_KEY_EXPERINCE, experince).apply();
    }

    @Override
    public void setTutorTargetedBoard(String board, String qualification, String qualifyArea) {
        mPrefs.edit().putString(PREF_KEY_BOARD, board).apply();
        mPrefs.edit().putString(PREF_KEY_QUALIFICATION, qualification).apply();
        mPrefs.edit().putString(PREF_KEY_AREA, qualifyArea).apply();
    }

    @Override
    public void setTutorCategory(String category) {
        mPrefs.edit().putString(PREF_KEY_TUTOR_CATEGORY, category).apply();
    }

    @Override
    public void setTutorBiodata(String overview) {
        mPrefs.edit().putString(PREF_KEY_BIODATA, overview).apply();
    }

    @Override
    public void setScreenNine(String classesPlace) {
        mPrefs.edit().putString(PREF_KEY_CLASSES_PLACE, classesPlace).apply();
    }

    @Override
    public void setScreenFour(String classes) {
        mPrefs.edit().putString(PREF_KEY_CLASSES, classes).apply();
    }

    @Override
    public void setSeniorTutor(boolean val) {
        mPrefs.edit().putBoolean(PREF_KEY_CLASSES, val).apply();
    }

    @Override
    public void setTutorSubject(String subjects) {
        mPrefs.edit().putString(PREF_KEY_SUBJECTS, subjects).apply();
    }
}