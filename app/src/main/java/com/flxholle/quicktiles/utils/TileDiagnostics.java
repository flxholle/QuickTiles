package com.flxholle.quicktiles.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static synchronized void clear(Context context) {
        try {
            context.getApplicationContext()
                    .getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit()
                    .remove(KEY_LOG)
                    .apply();
        } catch (Exception e) {
            Log.w(TAG, "Could not clear diagnostics", e);
        }
    }

    private static String stamp() {
        return new SimpleDateFormat("MM-dd HH:mm:ss.SSS", Locale.US).format(new Date());
    }
}
