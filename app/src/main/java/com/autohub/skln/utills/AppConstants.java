package com.autohub.skln.utills;

import com.autohub.skln.BuildConfig;

public interface AppConstants {
    int GPS_REQUEST = 3434;
    String PREF_NAME = "Skln_Prefs";
    //General Keys
    String KEY_DATA = BuildConfig.APPLICATION_ID + "_data_key_";
    String KEY_FRAGMENT = BuildConfig.APPLICATION_ID + "_fragment_name_key_";
    String KEY_URI = BuildConfig.APPLICATION_ID + "_uri_key_";
    String KEY_TYPE = BuildConfig.APPLICATION_ID + "_type_";
    String KEY_THEME = BuildConfig.APPLICATION_ID + "_theme_";

    // firestore db value constants
    String TYPE_TUTOR = "tutor";
    String TYPE_STUDENT = "student";
    String MALE = "male";
    String FEMALE = "female";
    String CATEGORY_ACADEMICS = "academics";
    String CATEGORY_HOBBY = "hobby";
    String CLASS_1 = "1";
    String CLASS_2 = "2";
    String CLASS_3 = "3";
    String CLASS_4 = "4";
    String CLASS_5 = "5";
    String CLASS_6 = "6";
    String CLASS_7 = "7";
    String CLASS_8 = "8";
    String CLASS_9 = "9";
    String CLASS_10 = "10";
    String CLASS_11 = "11";
    String CLASS_12 = "12";
    String HOBBY_GUITAR = "Guitar";
    String HOBBY_PAINT = "Painting";
    String HOBBY_MARTIAL = "Martial arts";
    String HOBBY_DRUM = "Drums and percussion";
    String HOBBY_KEYBOARD = "Keyboard";
    String HOBBY_DANCE = "Dance";
    String SUBJECT_SCIENCE = "Science";
    String SUBJECT_ENGLISH = "English";
    String SUBJECT_MATHS = "Maths";
    String SUBJECT_SOCIAL_STUDIES = "Social studies";
    String SUBJECT_LANGUAGES = "Languages";
    String SUBJECT_COMPUTER_SCIENCE = "Computer science";
    String SUBJECT_PHYSICS = "Physics";
    String SUBJECT_BIOLOGY = "Biology";
    String SUBJECT_CHEMISTRY = "Chemistry";
    String SUBJECT_BUSINESS = "Business studies";
    String SUBJECT_ACCOUNTANCY = "Accountancy";
    String SUBJECT_ECONOMICS = "Economics";
    String BOARD_CBSE = "CBSE";
    String BOARD_ICSE = "ICSE";
    String BOARD_STATE = "STATE BOARD";

    // constants used for encryption
    String ALGORITHM = "Blowfish";
    String MODE = "Blowfish/CBC/PKCS5Padding";
    String IV = "abcdefgh";
    String KEY = "MyKey";

    //ExploreFilterTypes
    String ACADMIC_SELECTION_FILTER = "acadmic_selection_filter";
    String GREAD_SELECTION_FILTER = "greadSelectionFiletr";
    String ALL_SELECTION_FILTER = "allselectionfilter";

    // firestore db keys - tutor
    String KEY_USER_ID = "id";
    String KEY_PHONE_NUMBER = "phoneNumber";
    String KEY_ACCOUNT_TYPE = "accountType";
    String KEY_PASSWORD = "password";
    String KEY_FIRST_NAME = "firstName";
    String KEY_LAST_NAME = "lastName";
    String KEY_EMAIL = "email";
    String KEY_SEX = "gender";
    String KEY_CATEGORY = "teachingCategory";
    String KEY_CLASSES = "classesToTeach";
    String KEY_SUBJECTS = "subjectsToTeach";
    String KEY_HOBBIES = "hobbiesToTeach";
    String KEY_PROFILE_PICTURE = "pictureUrl";
    String KEY_OCCUPATION = "occupation";
    String KEY_EXPERIENCE = "experience";
    String KEY_BOARD = "board";
    String KEY_QUALIFICATION = "qualification";
    String KEY_AREA_QUALIFICATION = "areaQualification";
    String KEY_BIODATA = "bioData";
    String KEY_CLASS_TYPE = "classType";
    String KEY_NO_OF_CLASSES = "noOfClasses";
    String KEY_CLASS_FREQUENCY = "classFrequency";
    String KEY_MAX_STUDENTS = "maxStudentsCapacity";
    String KEY_RATE = "rate";
    String KEY_PAYMENT_DURATION = "paymentDuration";
    String KEY_CITY = "city";
    String KEY_LATITUDE = "latitude";
    String KEY_LONGITUDE = "longitude";

    // firestore db keys - student
    String KEY_STDT_CLASS = "studentClass";
    String KEY_STDT_FAVORITE_CLASSES = "favoriteClasses";
    String KEY_STDT_LEAST_FAV_CLASSES = "leastFavoriteClasses";
    String KEY_STDT_HOBBIES = "hobbiesToPursue";

    // bundel keys
    String KEY_USERMAP = "usermap";

}
