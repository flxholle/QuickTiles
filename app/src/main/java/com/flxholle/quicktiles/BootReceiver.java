package com.flxholle.quicktiles;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.quicksettings.TileService;
import android.util.ArrayMap;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Map;
import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "QuickTiles";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!Objects.equals(action, Intent.ACTION_BOOT_COMPLETED)
                && !Objects.equals(action, Intent.ACTION_MY_PACKAGE_REPLACED)) {
            return;
        }

        try {
            // Need update tile after boot (and after the app was updated)
            ArrayMap<String, Class<?>> preferencesServices = SettingsActivity.getPreferenceService();
            Map<String, ?> preferences = PreferenceManager.getDefaultSharedPreferences(context).getAll();
            PackageManager pm = context.getPackageManager();
            for (Map.Entry<String, Class<?>> entry : preferencesServices.entrySet()) {
                Object switchPreference = preferences.get(entry.getKey());
                if (switchPreference != null && switchPreference.equals(Boolean.TRUE)) {
                    final ComponentName serviceClass = new ComponentName(context, entry.getValue());
                    pm.setComponentEnabledSetting(serviceClass,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    TileService.requestListeningState(context, serviceClass);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not restore tiles after " + action, e);
        }
    }
}
