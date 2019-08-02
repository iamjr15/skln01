package com.autohub.skln.models;

import android.databinding.BaseObservable;
import android.text.TextUtils;

import com.autohub.skln.utills.CommonUtils;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class UserViewModel extends BaseObservable {
    private User mUser;

    public UserViewModel(User user) {
        mUser = user;
    }

    public String getFirstName() {
        return mUser.firstName;
    }

    public String getLastName() {
        return CommonUtils.getString(mUser.lastName);
    }

    public String getClassesWithAffix() {
        return mUser.getClassesWithAffix();
    }

    public String getClassesToTeach() {
        return CommonUtils.getString(mUser.classesToTeach);
    }

    public String getSubjectsToTeach() {
        return CommonUtils.getString(mUser.subjectsToTeach);
    }

    public String getSubjectOrHobbiesToTeach() {
        if (TextUtils.isEmpty(getSubjectsToTeach())) {
            return getHobbiesToTeach();
        }
        return getSubjectsToTeach();
    }

    public String getHobbiesToTeach() {
        return CommonUtils.getString(mUser.hobbiesToTeach);
    }

    public String getOccupation() {
        return CommonUtils.getString(mUser.occupation);
    }

    public String getTeachingExp() {
        return CommonUtils.getString(mUser.teachingExp);
    }

    public String getQualification() {
        return CommonUtils.getString(mUser.qualification);
    }

    public String getAreaQualification() {
        return CommonUtils.getString(mUser.areaQualification);
    }

    public String getBoard() {
        return CommonUtils.getString(mUser.board);
    }

    public String getBioData() {
        return CommonUtils.getString(mUser.bioData);
    }

    public void setUser(User user) {
        this.mUser = user;
        notifyChange();
    }

    public void setBioData(String bio) {
        mUser.bioData = bio;
    }

    public User getUser() {
        return mUser;
    }

    public void setClasses(String classesToTeach) {
        mUser.classesToTeach = classesToTeach;
        notifyChange();
    }
}
