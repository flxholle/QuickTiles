package com.asdoi.quicksettings.utils;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

public class CustomAccessibilityService extends AccessibilityService {
    public static final String POWER_DIALOG = "power_dialog";
    public static final String LOCK_SCREEN = "lock_screen";
    public static final String TAKE_SCREENSHOT = "screenshot";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            if (POWER_DIALOG.equals(intent.getAction())) {
                showPowerDialog();
            } else if (LOCK_SCREEN.equals(intent.getAction())) {
                lockScreen();
            } else if (TAKE_SCREENSHOT.equals(intent.getAction())) {
                takeScreenshot();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    // screenshot, API>=P
    private void takeScreenshot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT);
        }
    }

    // sleep, API >=P
    private void lockScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
        }
    }

    // system power dialog
    private void showPowerDialog() {
        performGlobalAction(android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_POWER_DIALOG);
    }
}
