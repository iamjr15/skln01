package com.autohub.skln.utills;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-08-02.
 */
public class CommonUtils {
    private static final HashMap<String, String> ROMAN_CLASSES = new HashMap<>();

    static {
        ROMAN_CLASSES.put("1", "I");
        ROMAN_CLASSES.put("2", "II");
        ROMAN_CLASSES.put("3", "III");
        ROMAN_CLASSES.put("4", "IV");
        ROMAN_CLASSES.put("5", "V");
        ROMAN_CLASSES.put("6", "VI");
        ROMAN_CLASSES.put("7", "VII");
        ROMAN_CLASSES.put("8", "VIII");
        ROMAN_CLASSES.put("9", "IX");
        ROMAN_CLASSES.put("10", "X");
        ROMAN_CLASSES.put("11", "XI");
        ROMAN_CLASSES.put("12", "XII");
    }

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

    public static String getRomanCLass(String studentCLass) {
        return ROMAN_CLASSES.get(studentCLass.trim());
    }

    public static <T> boolean hasLowerClasses(T set, Function<T> function) {
        return function.apply(set);
    }

    public interface Function<T> {
        boolean apply(T data);
    }
}
