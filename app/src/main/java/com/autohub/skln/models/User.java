package com.autohub.skln.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
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

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class User implements Parcelable, AppConstants {
    private static final HashMap<String, Integer> FAVORITE_CLASSES = new HashMap<>();
    private static final HashMap<String, Integer> HOBBIES = new HashMap<>();

    static {
        FAVORITE_CLASSES.put(SUBJECT_SCIENCE, R.drawable.tile_science);
        FAVORITE_CLASSES.put(SUBJECT_ENGLISH, R.drawable.tile_english);
        FAVORITE_CLASSES.put(SUBJECT_MATHS, R.drawable.tile_maths);
        FAVORITE_CLASSES.put(SUBJECT_SOCIAL_STUDIES, R.drawable.tile_social);
        FAVORITE_CLASSES.put(SUBJECT_LANGUAGES, R.drawable.tile_languages);
        FAVORITE_CLASSES.put(SUBJECT_COMPUTER_SCIENCE, R.drawable.tile_computer);
        FAVORITE_CLASSES.put(SUBJECT_PHYSICS, R.drawable.tile_physics);
        FAVORITE_CLASSES.put(SUBJECT_BIOLOGY, R.drawable.tile_biology);
        FAVORITE_CLASSES.put(SUBJECT_CHEMISTRY, R.drawable.tile_chemistry);
        FAVORITE_CLASSES.put(SUBJECT_BUSINESS, R.drawable.tile_business);
        FAVORITE_CLASSES.put(SUBJECT_ACCOUNTANCY, R.drawable.tile_accountancy);
        FAVORITE_CLASSES.put(SUBJECT_ECONOMICS, R.drawable.tile_economics);

        HOBBIES.put(HOBBY_DANCE, R.drawable.tile_dance);
        HOBBIES.put(HOBBY_DRUM, R.drawable.tile_drum);
        HOBBIES.put(HOBBY_GUITAR, R.drawable.tile_guitar);
        HOBBIES.put(HOBBY_KEYBOARD, R.drawable.tile_keyboard);
        HOBBIES.put(HOBBY_MARTIAL, R.drawable.tile_martial);
        HOBBIES.put(HOBBY_PAINT, R.drawable.tile_painting);
    }

    public String id;
    public String firstName;
    public String lastName;
    public String classesToTeach;
    public String subjectsToTeach;
    public String hobbiesToTeach;
    public String occupation;
    public String experience;
    public String qualification;
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
    public float latitude;
    public float longitude;

    public User() {
    }

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

    @DrawableRes
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

    @DrawableRes
    public List<Integer> getHobbies() {
        List<Integer> hobbies = new ArrayList<>();
        if (TextUtils.isEmpty(hobbiesToPursue)) return hobbies;
        String[] selectedHobbies = hobbiesToPursue.split(",");
        for (String hobby : selectedHobbies) {
            if (HOBBIES.containsKey(hobby.trim()))
                hobbies.add(HOBBIES.get(hobby.trim()));
        }
        return hobbies;
    }

    public List<String> getSubjectsToTeachAsArray() {
        if (TextUtils.isEmpty(subjectsToTeach)) {
            return new ArrayList<>();
        }
        String[] subjects = subjectsToTeach.split(",");
        return Arrays.asList(subjects);
    }
}
