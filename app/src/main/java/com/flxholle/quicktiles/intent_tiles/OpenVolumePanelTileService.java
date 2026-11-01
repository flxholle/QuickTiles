package com.flxholle.quicktiles.intent_tiles;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.flxholle.quicktiles.VolumePanelActivity;
import com.flxholle.quicktiles.abstract_tiles.BaseTileService;

public class OpenVolumePanelTileService extends BaseTileService {

    /**
     * Shows the big volume panel (media, ring, notification, alarm, call) instead of the short
     * media slider. Off by default.
     */
    public static final String PREF_EXPANDED = "volume_panel_expanded";

    /**
     * Activity action that opens that big panel, see {@code Settings.Panel.ACTION_VOLUME} (API 29).
     * The value is written out instead of using the constant so the tile stays loadable on every
     * supported Android version.
     */
    private static final String ACTION_VOLUME = "android.settings.panel.action.VOLUME";

    @Override
    protected void onTileClicked() {
        boolean expanded = false;
        try {
            expanded = PreferenceManager.getDefaultSharedPreferences(this)
                    .getBoolean(PREF_EXPANDED, false);
        } catch (Exception ignored) {
            // Default to the compact panel
        }

        if (expanded) {
            record("volume panel: showing all streams");
            if (openAllStreamsPanel()) {
                return;
            }
        } else {
            record("volume panel: showing the compact media slider");
            if (openCompactPanel()) {
                return;
            }
        }

        // Whatever the setting asked for failed - try the other variant before giving up
        if (expanded ? openCompactPanel() : openAllStreamsPanel()) {
            return;
        }
        record("volume panel: falling back to the volume key simulation");
        showVolumeUi();
    }

    /**
     * Opens the short media volume slider through {@link VolumePanelActivity}. The activity is
     * transparent and finishes immediately; it only exists because SystemUI ignores the
     * {@link AudioManager#FLAG_SHOW_UI} request of an app that is not in the foreground.
     */
    private boolean openCompactPanel() {
        try {
            Intent intent = new Intent(this, VolumePanelActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityFromTile(intent);
            return true;
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not open the compact volume panel", e);
            record("volume panel: could not open the compact panel ("
                    + e.getClass().getSimpleName() + ")");
            return false;
        }
    }

    /**
     * Opens the big volume panel of the settings app.
     */
    private boolean openAllStreamsPanel() {
        try {
            Intent intent = new Intent(ACTION_VOLUME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) == null) {
                record("volume panel: no system activity handles " + ACTION_VOLUME);
                return false;
            }
            startActivityFromTile(intent);
            return true;
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not open the volume panel", e);
            record("volume panel: could not open the all streams panel ("
                    + e.getClass().getSimpleName() + ")");
            return false;
        }
    }

    /**
     * Android 14 dropped {@code startActivityAndCollapse(Intent)} and restricts background
     * activity starts, so the {@link PendingIntent} variant is used wherever it exists.
     */
    private void startActivityFromTile(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            startActivityAndCollapse(pendingIntent);
        } else {
            startActivityAndCollapse(intent);
        }
    }

    /**
     * Last resort: ask the audio service to show its own UI. This only works while the app is in
     * the foreground, but it is better than nothing.
     */
    private void showVolumeUi() {
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            if (audioManager == null) {
                record("volume panel: no AudioManager");
                return;
            }
            audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_SAME,
                    AudioManager.FLAG_SHOW_UI);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not show the volume UI", e);
            record("volume panel: could not show the volume UI ("
                    + e.getClass().getSimpleName() + ")");
        }
    }

    @Override
    public void reset() {
    }
}
