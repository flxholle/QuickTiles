package com.asdoi.quicksettings.utils;

import android.content.Context;

import androidx.preference.PreferenceManager;

public class SharedPreferencesUtil {
    public static String COUNTER = "counter_value";
    public static String CUSTOMIZE_KEEP_SCREEN_ON = "keep_screen_on_customize";
    public static String SCREEN_TIMEOUT = "screen_timeout_save";

    public static int getCounter(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(COUNTER, 0);
    }

    public static void triggerCounter(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(COUNTER, getCounter(context) + 1)
                .apply();
    }

    public static void resetCounter(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(COUNTER, 0)
                .apply();
    }

    public static int getCustomizeKeepScreenOn(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(CUSTOMIZE_KEEP_SCREEN_ON, "3"));
    }

    public static int getScreenTimeout(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(SCREEN_TIMEOUT, 60000);
    }

    public static void setScreenTimeout(Context context, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(SCREEN_TIMEOUT, value)
                .apply();
    }
}
