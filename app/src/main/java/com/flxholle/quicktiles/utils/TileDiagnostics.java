package com.flxholle.quicktiles.utils;

import android.app.ActivityManager;
import android.app.ApplicationExitInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Keeps a small ring buffer of the last tile callbacks, so that a user can tell (and share) what
 * actually happened when a tile was tapped. This is what makes it possible to distinguish between
 * "the system never even bound our service" and "we were called but something failed", which look
 * exactly the same from the outside: the tile does nothing.
 * <p>
 * The log is stored in SharedPreferences and shown in the app's settings screen.
 */
public final class TileDiagnostics {

    public static final String TAG = "QuickTiles";

    private static final String PREFS = "quicktiles_diagnostics";
    private static final String KEY_LOG = "log";
    private static final int MAX_LINES = 120;

    private static boolean processStartLogged = false;

    private TileDiagnostics() {
    }

    /**
     * Records an event. Newest entries are at the top. Never throws.
     */
    public static synchronized void record(Context context, String event) {
        Log.i(TAG, event);
        try {
            Context appContext = context.getApplicationContext();
            SharedPreferences prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

            StringBuilder builder = new StringBuilder();
            if (!processStartLogged) {
                processStartLogged = true;
                builder.append(stamp())
                        .append("  --- app process started (pid ")
                        .append(android.os.Process.myPid())
                        .append(") ---")
                        .append('\n');
            }
            builder.append(stamp()).append("  ").append(event).append('\n');
            builder.append(prefs.getString(KEY_LOG, ""));

            String[] lines = builder.toString().split("\n");
            StringBuilder trimmed = new StringBuilder();
            int count = Math.min(lines.length, MAX_LINES);
            for (int i = 0; i < count; i++) {
                trimmed.append(lines[i]).append('\n');
            }

            prefs.edit().putString(KEY_LOG, trimmed.toString()).apply();
        } catch (Exception e) {
            Log.w(TAG, "Could not store diagnostics", e);
        }
    }

    /**
     * Records an event together with the exception that caused it.
     */
    public static void recordError(Context context, String event, Throwable error) {
        String message = error == null ? "" : " (" + error.getClass().getSimpleName()
                + (error.getMessage() == null ? "" : ": " + error.getMessage()) + ")";
        record(context, event + message);
    }

    /**
     * Records how and when the previous app process ended.
     * <p>
     * This is the key piece of information for the "tiles only work while the app is open"
     * problem: if the system killed the app when it was swiped out of the recent apps, the next
     * start reports {@code REASON_USER_REQUESTED} (or, on some OEM builds, {@code REASON_OTHER}).
     * A killed-but-not-stopped app is still startable in the background, so its tiles keep
     * working; an app that was <em>stopped</em> (force stop) cannot start any component at all
     * until it is launched again. Never throws.
     */
    public static void recordProcessExitHistory(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return;
        }
        try {
            ActivityManager activityManager =
                    (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager == null) {
                return;
            }
            List<ApplicationExitInfo> exits = activityManager.getHistoricalProcessExitReasons(
                    context.getPackageName(), 0, 3);
            if (exits == null || exits.isEmpty()) {
                record(context, "no previous process exit recorded");
                return;
            }
            for (ApplicationExitInfo exit : exits) {
                record(context, "previous process ended " + stamp(exit.getTimestamp())
                        + " reason=" + reasonName(exit.getReason())
                        + " importance=" + exit.getImportance());
            }
        } catch (Throwable error) {
            Log.w(TAG, "Could not read process exit history", error);
        }
    }

    private static String reasonName(int reason) {
        switch (reason) {
            case ApplicationExitInfo.REASON_EXIT_SELF:
                return "EXIT_SELF";
            case ApplicationExitInfo.REASON_SIGNALED:
                return "SIGNALED (killed, e.g. by the task killer or the low memory killer)";
            case ApplicationExitInfo.REASON_LOW_MEMORY:
                return "LOW_MEMORY";
            case ApplicationExitInfo.REASON_CRASH:
                return "CRASH (see AndroidRuntime in logcat)";
            case ApplicationExitInfo.REASON_CRASH_NATIVE:
                return "CRASH_NATIVE";
            case ApplicationExitInfo.REASON_ANR:
                return "ANR";
            case ApplicationExitInfo.REASON_INITIALIZATION_FAILURE:
                return "INITIALIZATION_FAILURE";
            case ApplicationExitInfo.REASON_PERMISSION_CHANGE:
                return "PERMISSION_CHANGE";
            case ApplicationExitInfo.REASON_EXCESSIVE_RESOURCE_USAGE:
                return "EXCESSIVE_RESOURCE_USAGE";
            case ApplicationExitInfo.REASON_USER_REQUESTED:
                return "USER_REQUESTED (the phone stopped the app, e.g. when it was swiped out of the recent apps)";
            case ApplicationExitInfo.REASON_OTHER:
                return "OTHER (usually a task killer or the OEM battery manager)";
            default:
                return "REASON_" + reason;
        }
    }

    /**
     * Records a fatal, uncaught exception with its stack trace.
     * <p>
     * A crash is the only way a tile can stay bound to nothing: the process dies while SystemUI is
     * waiting for it, SystemUI gives up on that tile and every later tap on it is silently
     * dropped. Since the crash happens while the app is in the background nobody sees it - so it
     * is written here, where it survives the process death and can be read after the next start.
     * Never throws.
     */
    public static void recordFatal(Context context, Thread thread, Throwable error) {
        try {
            String trace = "";
            if (error != null) {
                StackTraceElement[] frames = error.getStackTrace();
                StringBuilder builder = new StringBuilder();
                int count = Math.min(frames.length, 8);
                for (int i = 0; i < count; i++) {
                    if (i > 0) {
                        builder.append(" | ");
                    }
                    builder.append(frames[i].toString());
                }
                trace = builder.toString();
            }
            record(context, "FATAL in thread " + (thread == null ? "?" : thread.getName())
                    + ": " + (error == null ? "?" : error.getClass().getName()
                    + (error.getMessage() == null ? "" : ": " + error.getMessage()))
                    + (trace.isEmpty() ? "" : " at " + trace));
        } catch (Throwable ignored) {
            // A crash handler must never crash
        }
    }

    /**
     * @return the most recent fatal error line, or an empty string.
     */
    public static String lastFatal(Context context) {
        String log = read(context);
        for (String line : log.split("\n")) {
            if (line.contains("FATAL in thread ")) {
                return line.trim();
            }
        }
        return "";
    }

    /**
     * @return the recorded events, newest first (may be empty).
     */
    public static synchronized String read(Context context) {
        try {
            return context.getApplicationContext()
                    .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .getString(KEY_LOG, "");
        } catch (Exception e) {
            Log.w(TAG, "Could not read diagnostics", e);
            return "";
        }
    }

    private static String stamp() {
        return stamp(System.currentTimeMillis());
    }

    private static String stamp(long time) {
        return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US).format(new Date(time));
    }
}
