package com.asdoi.quicksettings.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;

public class WriteSystemSettingsUtils {
    private static final String DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final String DISPLAY_DALTONIZER = "accessibility_display_daltonizer";

    public static boolean isGreyscaleEnable(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.Secure.getInt(contentResolver, DISPLAY_DALTONIZER_ENABLED, 0) == 1
                && Settings.Secure.getInt(contentResolver, DISPLAY_DALTONIZER, 0) == 0;
    }

    public static void toggleGreyscale(Context context, boolean greyscale) {
        ContentResolver contentResolver = context.getContentResolver();
        Settings.Secure.putInt(contentResolver, DISPLAY_DALTONIZER_ENABLED, greyscale ? 1 : 0);
        Settings.Secure.putInt(contentResolver, DISPLAY_DALTONIZER, greyscale ? 0 : -1);
    }

    public static int getIntFromGlobalSettings(ContentResolver contentResolver, String key) {
        return Settings.Global.getInt(contentResolver, key, 0);
    }

    public static boolean setIntToGlobalSettings(ContentResolver contentResolver, String key, int value) {
        return Settings.Global.putInt(contentResolver, key, value);
    }

    public static float getFloatFromGlobalSettings(ContentResolver contentResolver, String key) {
        return Settings.Global.getFloat(contentResolver, key, 0f);
    }

    public static boolean setFloatToGlobalSettings(ContentResolver contentResolver, String key, float value) {
        return Settings.Global.putFloat(contentResolver, key, value);
    }

    public static int getIntFromSystemSettings(ContentResolver contentResolver, String key) {
        return Settings.System.getInt(contentResolver, key, 0);
    }

    public static boolean setIntToSystemSettings(ContentResolver contentResolver, String key, int value) {
        return Settings.System.putInt(contentResolver, key, value);
    }
}
