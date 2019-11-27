package com.autohub.skln.utills;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.HashSet;


/**
 * Modified by Vt Netzwelt
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
        if (n == 11 || n == 12) {
            return "th";
        }
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

    public static String getGrade(int n) {
        return n + getClassSuffix(n) + " Grade";
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

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static String getImageFilePath(Uri uri, Context context) {


        String wholeID = DocumentsContract.getDocumentId(uri);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }
/*String wholeID = DocumentsContract.getDocumentId(uriThatYouCurrentlyHave);

// Split at colon, use second item in the array
String id = wholeID.split(":")[1];

String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
String sel = MediaStore.Images.Media._ID + "=?";

Cursor cursor = getContentResolver().
                          query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                          column, sel, new String[]{ id }, null);

String filePath = "";

int columnIndex = cursor.getColumnIndex(column[0]);

if (cursor.moveToFirst()) {
    filePath = cursor.getString(columnIndex);
}

cursor.close();*/
}
