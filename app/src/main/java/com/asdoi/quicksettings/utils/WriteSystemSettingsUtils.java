package com.asdoi.quicksettings.utils;

import android.content.ContentResolver;
import android.provider.Settings;

public class WriteSystemSettingsUtils {

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
