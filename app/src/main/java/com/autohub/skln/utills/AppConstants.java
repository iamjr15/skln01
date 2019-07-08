package com.autohub.skln.utills;

public class AppConstants {
    public static final String PREF_NAME = "Skln_Prefs";

    // firestore db value constants
    public static final String TYPE_TUTOR = "tutor";
    public static final String TYPE_STUDENT = "student";
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String CATEGORY_ACADEMICS = "academics";
    public static final String CATEGORY_HOBBY = "hobby";
    public static final String CLASS_1 = "Class 1";
    public static final String CLASS_2 = "Class 2";
    public static final String CLASS_3 = "Class 3";
    public static final String CLASS_4 = "Class 4";
    public static final String CLASS_5 = "Class 5";
    public static final String CLASS_6 = "Class 6";
    public static final String CLASS_7 = "Class 7";
    public static final String CLASS_8 = "Class 8";
    public static final String CLASS_9 = "Class 9";
    public static final String CLASS_10 = "Class 10";
    public static final String CLASS_11 = "Class 11";
    public static final String CLASS_12 = "Class 12";
    public static final String HOBBY_GUITAR = "Guitar";
    public static final String HOBBY_PAINT = "Painting";
    public static final String HOBBY_MARTIAL = "Martial arts";
    public static final String HOBBY_DRUM = "Drums and percussion";
    public static final String HOBBY_KEYBOARD = "Keyboard";
    public static final String HOBBY_DANCE = "Dance";
    public static final String SUBJECT_SCIENCE = "Science";
    public static final String SUBJECT_ENGLISH = "English";
    public static final String SUBJECT_MATHS = "Maths";
    public static final String SUBJECT_SOCIAL_STUDIES = "Social studies";
    public static final String SUBJECT_LANGUAGES = "Languages";
    public static final String SUBJECT_COMPUTER_SCIENCE = "Computer science";
    public static final String SUBJECT_PHYSICS = "Physics";
    public static final String SUBJECT_BIOLOGY = "Biology";
    public static final String SUBJECT_CHEMISTRY = "Chemistry";
    public static final String SUBJECT_BUSINESS = "Business studies";
    public static final String SUBJECT_ACCOUNTANCY = "Accountancy";
    public static final String SUBJECT_ECONOMICS = "Economics";
    public static final String BOARD_CBSE = "CBSE";
    public static final String BOARD_ICSE = "ICSE";
    public static final String BOARD_STATE = "STATE BOARD";

    // constants used for encryption
    public static final String ALGORITHM = "Blowfish";
    public static final String MODE = "Blowfish/CBC/PKCS5Padding";
    public static final String IV = "abcdefgh";
    public static final String KEY = "MyKey";

    // firestore db keys - tutor
    public static final String KEY_PHONE_NUMBER = "phone_number";
    public static final String KEY_ACCOUNT_TYPE = "account_type";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_SEX = "sex";
    public static final String KEY_CATEGORY = "teaching_category";
    public static final String KEY_CLASSES = "classes_to_teach";
    public static final String KEY_SUBJECTS = "subjects_to_teach";
    public static final String KEY_HOBBIES = "hobbies_to_teach";
    public static final String KEY_PROFILE_PICTURE = "picture_url";
    public static final String KEY_OCCUPATION = "occupation";
    public static final String KEY_EXPERIENCE = "experience";
    public static final String KEY_BOARD = "board";
    public static final String KEY_QUALIFICATION = "qualification";
    public static final String KEY_AREA_QUALIFICATION = "area_qualification";
    public static final String KEY_BIODATA = "biodata";
    public static final String KEY_CLASS_TYPE = "class_type";
    public static final String KEY_NO_OF_CLASSES = "no_of_classes";
    public static final String KEY_CLASS_FREQUENCY = "class_frequency";
    public static final String KEY_MAX_STUDENTS = "max_students_capacity";
    public static final String KEY_RATE = "rate";

    // firestore db keys - student
    public static final String KEY_STDT_CLASS = "class";
    public static final String KEY_STDT_FAVORITE_CLASSES = "favorite_classes";
    public static final String KEY_STDT_LEAST_FAV_CLASSES = "least_favorite_classes";
    public static final String KEY_STDT_HOBBIES = "hobbies_to_pursue";

}
