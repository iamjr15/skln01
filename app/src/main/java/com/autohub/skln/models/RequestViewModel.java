package com.autohub.skln.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;

import com.autohub.skln.utills.CommonUtils;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-09.
 */
public class RequestViewModel extends BaseObservable implements Parcelable {
    private Request mRequest;
    private final String mUserType;
    private final String mRequestId;

    public RequestViewModel(Request request, String userType, String requestId) {
        mRequest = request;
        mUserType = userType;
        mRequestId = requestId;
    }

    protected RequestViewModel(Parcel in) {
        mRequest = in.readParcelable(Request.class.getClassLoader());
        mUserType = in.readString();
        mRequestId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mRequest, flags);
        dest.writeString(mUserType);
        dest.writeString(mRequestId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RequestViewModel> CREATOR = new Creator<RequestViewModel>() {
        @Override
        public RequestViewModel createFromParcel(Parcel in) {
            return new RequestViewModel(in);
        }

        @Override
        public RequestViewModel[] newArray(int size) {
            return new RequestViewModel[size];
        }
    };

    public String getName() {
        if (mUserType.equalsIgnoreCase("student")) {
            return mRequest.tutorName;
        }
        return mRequest.studentName;
    }

    public String getUserType() {
        return mUserType;
    }

    public String getSubject() {
        return CommonUtils.getString(mRequest.subject);
    }

    public String getClassType() {
        return CommonUtils.getString(mRequest.classType);
    }

    public String getRequestId() {
        return mRequestId;
    }

    public String getStudentClass() {
        return CommonUtils.getRomanCLass(mRequest.studentClass);
    }

    public Request getRequest() {
        return mRequest;
    }
}
