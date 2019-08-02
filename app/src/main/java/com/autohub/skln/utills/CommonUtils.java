package com.autohub.skln.utills;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;

import java.util.HashSet;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class CommonUtils {
    public static String getClassSuffix(final int n) {
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    @NonNull
    public static String getString(@Nullable Editable text) {
        return (text == null || text.length() == 0) ? "" : text.toString();
    }

    @NonNull
    public static String getString(@Nullable String text) {
        return (text == null || text.length() == 0) ? "" : text;
    }

    public static String getClasses(HashSet<String> selectedClasses) {
        StringBuilder builder = new StringBuilder();
        for (String selectedClass : selectedClasses) {
            builder.append(selectedClass).append(",");
        }
        return builder.substring(0, builder.length() - 1);
    }

    public static <T> boolean hasLowerClasses(T set, Function<T> function) {
        return function.apply(set);
    }

    public interface Function<T> {
        boolean apply(T data);
    }
}
