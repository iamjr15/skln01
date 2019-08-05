package com.autohub.skln.utills;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.autohub.skln.FrameActivity;

/**
 * Created by m.imran
 * Senior Software Engineer at
 * BhimSoft on 2019-07-30.
 */
public class ActivityUtils {

    public static void launchActivity(@NonNull Context context, @NonNull Class clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    public static void launchActivity(@NonNull Context context, @NonNull Class clazz, @NonNull Bundle bundle) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(AppConstants.KEY_DATA, bundle);
        context.startActivity(intent);
    }
    public static void launchFragment(@NonNull Context context, @NonNull String fragmentName) {
        Intent intent = new Intent(context, FrameActivity.class);
        intent.putExtra(AppConstants.KEY_FRAGMENT, fragmentName);
        context.startActivity(intent);
    }

    public static void launchFragment(@NonNull Context context, @NonNull String fragmentName, @NonNull Bundle bundle) {
        Intent intent = new Intent(context, FrameActivity.class);
        intent.putExtra(AppConstants.KEY_FRAGMENT, fragmentName);
        intent.putExtra(AppConstants.KEY_DATA, bundle);
        context.startActivity(intent);
    }
}
