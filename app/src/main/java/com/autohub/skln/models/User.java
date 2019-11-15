package com.autohub.skln.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.autohub.skln.R;
import com.autohub.skln.utills.AppConstants;
import com.autohub.skln.utills.CommonUtils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Modified by Vt Netzwelt
 */

public class User implements Parcelable, AppConstants {
    private static final HashMap<String, Integer> FAVORITE_CLASSES = new HashMap<>();
    private static final HashMap<String, AcadmicsData> ACADMICS_DATA_HASH_MAP = new HashMap<>();
    private static final HashMap<String, AcadmicsData> ACADMICS_SENIORDATA_HASH_MAP = new HashMap<>();
    private static final HashMap<String, HobbiesData> HOBBIES_DATA_HASH_MAP = new HashMap<>();

    static {
        FAVORITE_CLASSES.put(SUBJECT_SCIENCE, R.drawable.microscope);
        FAVORITE_CLASSES.put(SUBJECT_ENGLISH, R.drawable.noun);
        FAVORITE_CLASSES.put(SUBJECT_MATHS, R.drawable.geometry);
        FAVORITE_CLASSES.put(SUBJECT_SOCIAL_STUDIES, R.drawable.strike);
        FAVORITE_CLASSES.put(SUBJECT_LANGUAGES, R.drawable.language);
        FAVORITE_CLASSES.put(SUBJECT_COMPUTER_SCIENCE, R.drawable.informatic);

        FAVORITE_CLASSES.put(SUBJECT_PHYSICS, R.drawable.physics);
        FAVORITE_CLASSES.put(SUBJECT_BIOLOGY, R.drawable.microscope);
        FAVORITE_CLASSES.put(SUBJECT_CHEMISTRY, R.drawable.test_tube);
        FAVORITE_CLASSES.put(SUBJECT_BUSINESS, R.drawable.rupee);
        FAVORITE_CLASSES.put(SUBJECT_ACCOUNTANCY, R.drawable.accounting);
        FAVORITE_CLASSES.put(SUBJECT_ECONOMICS, R.drawable.rating);


        ACADMICS_DATA_HASH_MAP.put(SUBJECT_SCIENCE,
                new AcadmicsData(R.color.science, SUBJECT_SCIENCE, R.drawable.microscope));
        ACADMICS_DATA_HASH_MAP.put(SUBJECT_ENGLISH,
                new AcadmicsData(R.color.english, SUBJECT_ENGLISH, R.drawable.noun));
        ACADMICS_DATA_HASH_MAP.put(SUBJECT_MATHS,
                new AcadmicsData(R.color.math, SUBJECT_MATHS, R.drawable.geometry));

        ACADMICS_DATA_HASH_MAP.put(SUBJECT_SOCIAL_STUDIES,
                new AcadmicsData(R.color.socialstudies, SUBJECT_SOCIAL_STUDIES, R.drawable.strike));
        ACADMICS_DATA_HASH_MAP.put(SUBJECT_LANGUAGES,
                new AcadmicsData(R.color.language, SUBJECT_LANGUAGES, R.drawable.language));
        ACADMICS_DATA_HASH_MAP.put(SUBJECT_COMPUTER_SCIENCE,
                new AcadmicsData(R.color.computerscience, SUBJECT_COMPUTER_SCIENCE, R.drawable.informatic));


        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_ENGLISH,
                new AcadmicsData(R.color.english, SUBJECT_ENGLISH, R.drawable.noun));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_MATHS,
                new AcadmicsData(R.color.math, SUBJECT_MATHS, R.drawable.geometry));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_COMPUTER_SCIENCE,
                new AcadmicsData(R.color.computerscience, SUBJECT_COMPUTER_SCIENCE, R.drawable.informatic));

        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_PHYSICS,
                new AcadmicsData(R.color.physics, SUBJECT_PHYSICS, R.drawable.physics));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_BIOLOGY,
                new AcadmicsData(R.color.biology, SUBJECT_BIOLOGY, R.drawable.microscope));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_CHEMISTRY,
                new AcadmicsData(R.color.chemistry, SUBJECT_CHEMISTRY, R.drawable.test_tube));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_BUSINESS,
                new AcadmicsData(R.color.business, SUBJECT_BUSINESS, R.drawable.rupee));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_ACCOUNTANCY,
                new AcadmicsData(R.color.account, SUBJECT_ACCOUNTANCY, R.drawable.accounting));
        ACADMICS_SENIORDATA_HASH_MAP.put(SUBJECT_ECONOMICS,
                new AcadmicsData(R.color.economics, SUBJECT_ECONOMICS, R.drawable.rating));


        HOBBIES_DATA_HASH_MAP.put(HOBBY_DANCE, new HobbiesData(R.color.dance, HOBBY_DANCE, R.drawable.dancing, false));
        HOBBIES_DATA_HASH_MAP.put(HOBBY_DRUM, new HobbiesData(R.color.drum, HOBBY_DRUM, R.drawable.drum, false));
        HOBBIES_DATA_HASH_MAP.put(HOBBY_GUITAR, new HobbiesData(R.color.guitar, HOBBY_GUITAR, R.drawable.guitar, false));
        HOBBIES_DATA_HASH_MAP.put(HOBBY_KEYBOARD, new HobbiesData(R.color.keyboard, HOBBY_KEYBOARD, R.drawable.piano, false));
        HOBBIES_DATA_HASH_MAP.put(HOBBY_MARTIAL, new HobbiesData(R.color.materialarts, HOBBY_MARTIAL, R.drawable.attack, false));
        HOBBIES_DATA_HASH_MAP.put(HOBBY_PAINT, new HobbiesData(R.color.painting, HOBBY_PAINT, R.drawable.brush, false));


    }

    public String id;
    public String firstName;
    public String lastName;
    public String classesToTeach;
    public String subjectsToTeach = "";
    public String hobbiesToTeach;
    public String occupation;
    public String experience;
    public String qualification = "";
    public String areaQualification;
    public String board;
    public String bioData;
    public String favoriteClasses;
    public String hobbiesToPursue;
    public String leastFavoriteClasses;
    public String gender;
    public String phoneNumber;
    public String accountType;
    public String teachingCategory;
    public String pictureUrl;
    public String classType;
    public String noOfClasses;
    public String classFrequency;
    public String maxStudentsCapacity;
    public String rate;
    public String studentClass;
    public String paymentDuration;
    public String city;
    public double distance = 0.0;
    public float latitude = 0.0f;
    public float longitude = 0.0f;

    public User() {
    }





   /* public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {
        return CommonUtils.getString(distance);
    }*/


    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        classesToTeach = in.readString();
        subjectsToTeach = in.readString();
        hobbiesToTeach = in.readString();
        occupation = in.readString();
        experience = in.readString();
        qualification = in.readString();
        areaQualification = in.readString();
        board = in.readString();
        bioData = in.readString();
        favoriteClasses = in.readString();
        hobbiesToPursue = in.readString();
        leastFavoriteClasses = in.readString();
        gender = in.readString();
        phoneNumber = in.readString();
        accountType = in.readString();
        teachingCategory = in.readString();
        pictureUrl = in.readString();
        classType = in.readString();
        noOfClasses = in.readString();
        classFrequency = in.readString();
        maxStudentsCapacity = in.readString();
        rate = in.readString();
        studentClass = in.readString();
        paymentDuration = in.readString();
        city = in.readString();
        distance = in.readDouble();

        latitude = in.readFloat();
        longitude = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(classesToTeach);
        dest.writeString(subjectsToTeach);
        dest.writeString(hobbiesToTeach);
        dest.writeString(occupation);
        dest.writeString(experience);
        dest.writeString(qualification);
        dest.writeString(areaQualification);
        dest.writeString(board);
        dest.writeString(bioData);
        dest.writeString(favoriteClasses);
        dest.writeString(hobbiesToPursue);
        dest.writeString(leastFavoriteClasses);
        dest.writeString(gender);
        dest.writeString(phoneNumber);
        dest.writeString(accountType);
        dest.writeString(teachingCategory);
        dest.writeString(pictureUrl);
        dest.writeString(classType);
        dest.writeString(noOfClasses);
        dest.writeString(classFrequency);
        dest.writeString(maxStudentsCapacity);
        dest.writeString(rate);
        dest.writeString(studentClass);
        dest.writeString(paymentDuration);
        dest.writeString(city);
        dest.writeDouble(distance);

        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public static User prepareUser(DocumentSnapshot documentSnapshot) {
        User user = new User();
        user.firstName = documentSnapshot.getString(KEY_FIRST_NAME);
        user.lastName = documentSnapshot.getString(KEY_LAST_NAME);
        user.classesToTeach = documentSnapshot.getString(KEY_CLASSES);
        user.subjectsToTeach = documentSnapshot.getString(KEY_SUBJECTS);
        user.hobbiesToTeach = documentSnapshot.getString(KEY_HOBBIES);
        user.occupation = documentSnapshot.getString(KEY_OCCUPATION);
        user.experience = documentSnapshot.getString(KEY_EXPERIENCE);
        user.qualification = documentSnapshot.getString(KEY_QUALIFICATION);
        user.areaQualification = documentSnapshot.getString(KEY_AREA_QUALIFICATION);
        user.board = documentSnapshot.getString(KEY_BOARD);
        user.bioData = documentSnapshot.getString(KEY_BIODATA);
        user.favoriteClasses = documentSnapshot.getString(KEY_STDT_FAVORITE_CLASSES);
        user.hobbiesToPursue = documentSnapshot.getString(KEY_STDT_HOBBIES);
        user.leastFavoriteClasses = documentSnapshot.getString(KEY_STDT_LEAST_FAV_CLASSES);
        user.gender = documentSnapshot.getString(KEY_SEX);
        return user;
    }

    public String getClassesWithAffix() {
        if (TextUtils.isEmpty(classesToTeach)) return "";
        StringBuilder builder = new StringBuilder();
        String[] classes = classesToTeach.split(",");
        for (String clazz : classes) {
            clazz = clazz.trim();
            builder.append(clazz).append(CommonUtils.getClassSuffix(Integer.parseInt(clazz))).append(", ");
        }
        return builder.substring(0, builder.length() - 2);
    }

    public HashSet<String> getClasses() {
        HashSet<String> clazzes = new HashSet<>();
        if (TextUtils.isEmpty(classesToTeach)) return clazzes;
        StringBuilder builder = new StringBuilder();
        String[] classes = classesToTeach.split(",");
        for (String clazz : classes) {
            clazz = clazz.trim();
            clazzes.add(clazz);
        }
        return clazzes;
    }

    public List<Integer> getFavoriteClasses() {
        List<Integer> classes = new ArrayList<>();
        if (TextUtils.isEmpty(favoriteClasses)) return classes;
        String[] subjects = favoriteClasses.split(",");
        for (String subj : subjects) {
            if (FAVORITE_CLASSES.containsKey(subj.trim()))
                classes.add(FAVORITE_CLASSES.get(subj.trim()));
        }
        if (!TextUtils.isEmpty(leastFavoriteClasses)) {
            subjects = leastFavoriteClasses.split(",");
            for (String subj : subjects) {
                if (FAVORITE_CLASSES.containsKey(subj.trim()))
                    classes.add(FAVORITE_CLASSES.get(subj.trim()));
            }
        }
        return classes;
    }


    public List<AcadmicsData> getAcadmics() {

        boolean isSeniorClass = studentClass.equals(CLASS_11) || studentClass.equals(CLASS_12);
        List<AcadmicsData> acadmicsDataList = new ArrayList<>();
//        if (TextUtils.isEmpty(favoriteClasses)) return acadmicsDataList;
//        String[] subjects = favoriteClasses.split(",");

        if (isSeniorClass) {
            for (Map.Entry<String, AcadmicsData> entry : ACADMICS_SENIORDATA_HASH_MAP.entrySet()) {
                acadmicsDataList.add(ACADMICS_SENIORDATA_HASH_MAP.get(entry.getKey()));
            }


        } else {

            for (Map.Entry<String, AcadmicsData> entry : ACADMICS_DATA_HASH_MAP.entrySet()) {
                acadmicsDataList.add(ACADMICS_DATA_HASH_MAP.get(entry.getKey()));
            }
        }



       /* for (String subj : subjects) {
//            if (ACADMICS_DATA_HASH_MAP.containsKey(subj.trim()))
            if (isSeniorClass) {
                acadmicsDataList.add(ACADMICS_SENIORDATA_HASH_MAP.get(subj.trim()));

            } else {
                acadmicsDataList.add(ACADMICS_SENIORDATA_HASH_MAP.get(subj.trim()));

            }

        }*/


        return acadmicsDataList;
    }


    public List<HobbiesData> getHobbies() {
        List<HobbiesData> hobbies = new ArrayList<>();
//        if (TextUtils.isEmpty(hobbiesToPursue)) return hobbies;
//        String[] selectedHobbies = hobbiesToPursue.split(",");

        for (Map.Entry<String, HobbiesData> entry : HOBBIES_DATA_HASH_MAP.entrySet()) {
            hobbies.add(HOBBIES_DATA_HASH_MAP.get(entry.getKey()));
        }



        /*for (String hobby : selectedHobbies) {
         *//* if (HOBBIES_DATA_HASH_MAP.containsKey(hobby.trim()))
                hobbies.add(HOBBIES_DATA_HASH_MAP.get(hobby.trim()));*//*
            hobbies.add(HOBBIES_DATA_HASH_MAP.get(hobby.trim()));


        }*/
        return hobbies;
    }


  /*  @DrawableRes
    public List<Integer> getHobbies() {
        List<Integer> hobbies = new ArrayList<>();
        if (TextUtils.isEmpty(hobbiesToPursue)) return hobbies;
        String[] selectedHobbies = hobbiesToPursue.split(",");
        for (String hobby : selectedHobbies) {
            if (HOBBIES.containsKey(hobby.trim()))
                hobbies.add(HOBBIES.get(hobby.trim()));
        }
        return hobbies;
    }*/

    public List<String> getSubjectsToTeachAsArray() {
        if (TextUtils.isEmpty(subjectsToTeach)) {
            return new ArrayList<>();
        }
        String[] subjects = subjectsToTeach.split(",");
        return Arrays.asList(subjects);
    }


}
