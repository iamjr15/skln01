package com.autohub.skln.utills;

import com.autohub.skln.BuildConfig;

public interface AppConstants {
    String PREF_NAME = "Skln_Prefs";
    //General Keys
    String KEY_DATA = BuildConfig.APPLICATION_ID + "_data_key_";
    String KEY_FRAGMENT = BuildConfig.APPLICATION_ID + "_fragment_name_key_";
    String KEY_URI = BuildConfig.APPLICATION_ID + "_uri_key_";

    // firestore db value constants
    String TYPE_TUTOR = "tutor";
    String TYPE_STUDENT = "student";
    String MALE = "male";
    String FEMALE = "female";
    String CATEGORY_ACADEMICS = "academics";
    String CATEGORY_HOBBY = "hobby";
    String CLASS_1 = "Class 1";
    String CLASS_2 = "Class 2";
    String CLASS_3 = "Class 3";
    String CLASS_4 = "Class 4";
    String CLASS_5 = "Class 5";
    String CLASS_6 = "Class 6";
    String CLASS_7 = "Class 7";
    String CLASS_8 = "Class 8";
    String CLASS_9 = "Class 9";
    String CLASS_10 = "Class 10";
    String CLASS_11 = "Class 11";
    String CLASS_12 = "Class 12";
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

    // firestore db keys - tutor
    String KEY_PHONE_NUMBER = "phone_number";
    String KEY_ACCOUNT_TYPE = "account_type";
    String KEY_PASSWORD = "password";
    String KEY_FIRST_NAME = "first_name";
    String KEY_LAST_NAME = "last_name";
    String KEY_SEX = "sex";
    String KEY_CATEGORY = "teaching_category";
    String KEY_CLASSES = "classes_to_teach";
    String KEY_SUBJECTS = "subjects_to_teach";
    String KEY_HOBBIES = "hobbies_to_teach";
    String KEY_PROFILE_PICTURE = "picture_url";
    String KEY_OCCUPATION = "occupation";
    String KEY_EXPERIENCE = "experience";
    String KEY_BOARD = "board";
    String KEY_QUALIFICATION = "qualification";
    String KEY_AREA_QUALIFICATION = "area_qualification";
    String KEY_BIODATA = "biodata";
    String KEY_CLASS_TYPE = "class_type";
    String KEY_NO_OF_CLASSES = "no_of_classes";
    String KEY_CLASS_FREQUENCY = "class_frequency";
    String KEY_MAX_STUDENTS = "max_students_capacity";
    String KEY_RATE = "rate";

    // firestore db keys - student
    String KEY_STDT_CLASS = "class";
    String KEY_STDT_FAVORITE_CLASSES = "favorite_classes";
    String KEY_STDT_LEAST_FAV_CLASSES = "least_favorite_classes";
    String KEY_STDT_HOBBIES = "hobbies_to_pursue";
}
