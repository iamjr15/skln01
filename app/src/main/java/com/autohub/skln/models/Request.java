package com.autohub.skln.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-05.
 */
public class Request implements Parcelable {
    public enum STATUS {
        PENDING("pending"), CANCELED("canceled"), ACCEPTED("accepted");

        private final String value;

        STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public String studentId;
    public String tutorId;
    public String subject;
    public String requestStatus;
    public String studentName;
    public String tutorName;
    public String studentClass;
    public String classType;

    public Request() {

    }

    public Request(String studentId, String tutorId, String subject, String tutorName, String studentName, String studentClass,String classType) {
        this.studentId = studentId;
        this.tutorId = tutorId;
        this.subject = subject;
        this.requestStatus = STATUS.PENDING.getValue();
        this.studentName = studentName;
        this.tutorName = tutorName;
        this.studentClass = studentClass;
        this.classType = classType;
    }

    protected Request(Parcel in) {
        studentId = in.readString();
        tutorId = in.readString();
        subject = in.readString();
        requestStatus = in.readString();
        studentName = in.readString();
        tutorName = in.readString();
        studentClass = in.readString();
        classType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(studentId);
        dest.writeString(tutorId);
        dest.writeString(subject);
        dest.writeString(requestStatus);
        dest.writeString(studentName);
        dest.writeString(tutorName);
        dest.writeString(studentClass);
        dest.writeString(classType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };
}
