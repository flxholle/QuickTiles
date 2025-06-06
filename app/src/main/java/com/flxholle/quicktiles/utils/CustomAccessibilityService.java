package com.flxholle.quicktiles.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.flxholle.quicktiles.R;

public class CustomAccessibilityService extends AccessibilityService {
    public static final String POWER_DIALOG = "power_dialog";
    public static final String LOCK_SCREEN = "lock_screen";
    public static final String TAKE_SCREENSHOT = "screenshot";
    public static final String TOGGLE_SPLIT_SCREEN = "toggle_split_screen";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            try {
                if (POWER_DIALOG.equals(intent.getAction())) {
                    showPowerDialog();
                } else if (LOCK_SCREEN.equals(intent.getAction())) {
                    lockScreen();
                } else if (TAKE_SCREENSHOT.equals(intent.getAction())) {
                    takeScreenshot();
                } else if (TOGGLE_SPLIT_SCREEN.equals(intent.getAction())) {
                    toggleSplitScreen();
                }
            } catch (Exception e) {
                Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_LONG).show();
            }
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
