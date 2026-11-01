package com.flxholle.quicktiles;

import android.app.Application;
import android.content.pm.PackageInfo;

import com.flxholle.quicktiles.utils.TileDiagnostics;

/**
 * Records when the app process starts, so the tile diagnostics log shows the gaps between
 * "the system started our process" and "the user opened the app".
 */
public class QuickTilesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TileDiagnostics.record(this, "app started, version " + versionName());
    }

    private String versionName() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            return "?";
        }
    }
}
