package com.autohub.skln.listeners;

import android.os.Parcel;
import android.os.Parcelable;

public class DialogFragmentButtonPressedListener<T> implements Parcelable {

    public DialogFragmentButtonPressedListener() {

    }

    private DialogFragmentButtonPressedListener(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DialogFragmentButtonPressedListener> CREATOR = new Creator<DialogFragmentButtonPressedListener>() {
        @Override
        public DialogFragmentButtonPressedListener createFromParcel(Parcel in) {
            return new DialogFragmentButtonPressedListener(in);
        }

        @Override
        public DialogFragmentButtonPressedListener[] newArray(int size) {
            return new DialogFragmentButtonPressedListener[size];
        }
    };

    public void onButtonPressed(T t) {

    }
}