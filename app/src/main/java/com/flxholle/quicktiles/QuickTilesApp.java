package com.flxholle.quicktiles;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.os.Bundle;

import com.flxholle.quicktiles.utils.TileDiagnostics;

/**
 * Records what happens to the app process itself: when it starts, how the previous process ended,
 * when it goes to the background and - most importantly - why it died if it crashed.
 * <p>
 * A crash in the background is invisible to the user but it is exactly what makes Quick Settings
 * tiles die: SystemUI binds the tile service, the process crashes while starting, SystemUI gives up
 * on that tile and every later tap on it is dropped without any feedback.
 */
public class QuickTilesApp extends Application {

    private int startedActivities = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        installCrashHandler();
        registerLifecycleLogging();

        TileDiagnostics.recordProcessExitHistory(this);
        TileDiagnostics.record(this, "app started, version " + versionName());
    }

    /**
     * Writes uncaught exceptions into the diagnostics log before the process goes down, so the
     * crash can be read (and reported) after the next start.
     */
    private void installCrashHandler() {
        final Thread.UncaughtExceptionHandler previous = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((thread, error) -> {
            TileDiagnostics.recordFatal(this, thread, error);
            if (previous != null) {
                previous.uncaughtException(thread, error);
            }
        });
    }

    /**
     * Records when the app goes to the background and when it comes back, so a gap in the
     * diagnostics log can be told apart from "the app was still running but the tile was never
     * called".
     */
    private void registerLifecycleLogging() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStarted(Activity activity) {
                if (startedActivities++ == 0) {
                    TileDiagnostics.record(QuickTilesApp.this, "app in foreground");
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {
                if (startedActivities > 0 && --startedActivities == 0) {
                    TileDiagnostics.record(QuickTilesApp.this,
                            "app went to background (process still cached)");
                }
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            @Override
            public void onActivityResumed(Activity activity) {
            }

            @Override
            public void onActivityPaused(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
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
