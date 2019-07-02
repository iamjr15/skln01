package com.autohub.skln.pref;


public interface PreferencesHelper {

    String getUserFirstName();

    String getUserLastName();

    String getUserPhone();

    String getUserPassword();

    String getUserId();

    String getOccupation();

    String getExperince();

    String getQualification();

    String getArea();

    String getUniversity();

    String getSpecialisation();

    String getYear();

    String getboard();

    String getexperinceLevel();

    String getworkingHour();

    String getOverview();

    String getClassesPlace();

    String getClasses();

    String getSubjects();

    String getProfileImage();

    String getCertificatesImage();

    String getProofImage();

    void setTutorPrimaryInfo(String firstName, String lastName, String password);

    void setUserId(String id);

    void setUserPhoneNumber(String phone);

    void setTutorPicture(String profileImage, String occupation, String experince);


    void setTutorTargetedBoard(String board, String experinceLevel, String workingHour);

//    void setUserScreenSevenForHobbies(String experinceLevel, String workingHour);

    void setTutorCategory(String category);

    void setTutorBiodata(String overview);

    void setScreenNine(String classesPlace);

    void setScreenFour(String classes);

    void setSeniorTutor(boolean val);

    void setTutorSubject(String subjects);
}