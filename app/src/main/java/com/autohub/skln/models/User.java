package com.autohub.skln.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.autohub.skln.utills.CommonUtils;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.autohub.skln.utills.AppConstants.KEY_AREA_QUALIFICATION;
import static com.autohub.skln.utills.AppConstants.KEY_BIODATA;
import static com.autohub.skln.utills.AppConstants.KEY_BOARD;
import static com.autohub.skln.utills.AppConstants.KEY_CLASSES;
import static com.autohub.skln.utills.AppConstants.KEY_EXPERIENCE;
import static com.autohub.skln.utills.AppConstants.KEY_FIRST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_HOBBIES;
import static com.autohub.skln.utills.AppConstants.KEY_LAST_NAME;
import static com.autohub.skln.utills.AppConstants.KEY_OCCUPATION;
import static com.autohub.skln.utills.AppConstants.KEY_QUALIFICATION;
import static com.autohub.skln.utills.AppConstants.KEY_SUBJECTS;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class User implements Parcelable {
    public String firstName;
    public String lastName;
    public String classesToTeach;
    public String subjectsToTeach;
    public String hobbiesToTeach;
    public String occupation;
    public String teachingExp;
    public String qualification;
    public String areaQualification;
    public String board;
    public String bioData;

    public User() {

    }

    protected User(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        classesToTeach = in.readString();
        subjectsToTeach = in.readString();
        hobbiesToTeach = in.readString();
        occupation = in.readString();
        teachingExp = in.readString();
        qualification = in.readString();
        areaQualification = in.readString();
        board = in.readString();
        bioData = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(classesToTeach);
        dest.writeString(subjectsToTeach);
        dest.writeString(hobbiesToTeach);
        dest.writeString(occupation);
        dest.writeString(teachingExp);
        dest.writeString(qualification);
        dest.writeString(areaQualification);
        dest.writeString(board);
        dest.writeString(bioData);
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
        user.teachingExp = documentSnapshot.getString(KEY_EXPERIENCE);
        user.qualification = documentSnapshot.getString(KEY_QUALIFICATION);
        user.areaQualification = documentSnapshot.getString(KEY_AREA_QUALIFICATION);
        user.board = documentSnapshot.getString(KEY_BOARD);
        user.bioData = documentSnapshot.getString(KEY_BIODATA);
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
}
