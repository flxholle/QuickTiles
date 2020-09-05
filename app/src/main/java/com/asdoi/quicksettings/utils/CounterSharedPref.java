package com.asdoi.quicksettings.utils;

import android.content.Context;

import androidx.preference.PreferenceManager;

public class CounterSharedPref {
    public static String COUNTER = "counter_value";

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
}
