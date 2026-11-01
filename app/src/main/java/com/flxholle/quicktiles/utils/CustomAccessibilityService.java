package com.flxholle.quicktiles.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.flxholle.quicktiles.R;

public class CustomAccessibilityService extends AccessibilityService {
    private static final String TAG = "QuickTiles";

    public static final String POWER_DIALOG = "power_dialog";
    public static final String LOCK_SCREEN = "lock_screen";
    public static final String TAKE_SCREENSHOT = "screenshot";
    public static final String TOGGLE_SPLIT_SCREEN = "toggle_split_screen";

    /**
     * The system keeps exactly one instance of this service alive as long as it is enabled, so a
     * static reference can be used to trigger its global actions from the tiles. This is much
     * more reliable than starting the service with Context#startService from a TileService,
     * because background service starts are blocked when the app is not in the foreground
     * (which is exactly the case when the user taps a tile after closing the app).
     */
    private static volatile CustomAccessibilityService instance;

    public static CustomAccessibilityService getInstance() {
        return instance;
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (instance == this) {
            instance = null;
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (instance == this) {
            instance = null;
        }
        super.onDestroy();
    }

    /**
     * Runs one of the global actions of this accessibility service.
     *
     * @return true if the action was dispatched
     */
    public boolean performAction(String action) {
        if (action == null) {
            return false;
        }
        try {
            switch (action) {
                case POWER_DIALOG:
                    showPowerDialog();
                    return true;
                case LOCK_SCREEN:
                    lockScreen();
                    return true;
                case TAKE_SCREENSHOT:
                    takeScreenshot();
                    return true;
                case TOGGLE_SPLIT_SCREEN:
                    toggleSplitScreen();
                    return true;
                default:
                    Log.w(TAG, "Unknown accessibility action: " + action);
                    return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not perform accessibility action " + action, e);
            Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            performAction(intent.getAction());
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // screenshot, API>=P
    private void takeScreenshot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT);
        }
    }

    // sleep, API >=P
    private void lockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
        }
    }

    // system power dialog
    private void showPowerDialog() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
    }

    private void toggleSplitScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            performGlobalAction(AccessibilityService.GLOBAL_ACTION_TOGGLE_SPLIT_SCREEN);
        }
    }
}
