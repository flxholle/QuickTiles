package com.flxholle.quicktiles.intent_tiles;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import com.flxholle.quicktiles.abstract_tiles.BaseTileService;

public class OpenVolumePanelTileService extends BaseTileService {

    /**
     * Activity action that opens the system volume panel, see
     * {@code Settings.Panel.ACTION_VOLUME} (API 29). The value is written out instead of using the
     * constant so the tile stays loadable on every supported Android version.
     */
    private static final String ACTION_VOLUME = "android.settings.panel.action.VOLUME";

    /**
     * Opens the volume panel.
     * <p>
     * The previous implementation only called
     * {@code adjustStreamVolume(STREAM_MUSIC, ADJUST_SAME, FLAG_SHOW_UI)}. Since Android 13 the
     * platform silently strips {@link AudioManager#FLAG_SHOW_UI} from every call made by an app
     * that is not in the foreground (AudioService only shows the UI for the platform uid and for
     * foreground apps). The tile therefore reported a normal click - no exception, nothing to log -
     * but no panel ever appeared unless the app happened to be open. The volume panel has to be
     * started as an activity instead, which is what the system's own volume tile does.
     */
    @Override
    protected void onTileClicked() {
        if (openVolumePanel()) {
            return;
        }
        record("volume panel: falling back to the volume key simulation");
        showVolumeUi();
    }

    private boolean openVolumePanel() {
        try {
            Intent intent = new Intent(ACTION_VOLUME).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (intent.resolveActivity(getPackageManager()) == null) {
                record("volume panel: no system activity handles " + ACTION_VOLUME);
                return false;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                startActivityAndCollapse(pendingIntent);
            } else {
                startActivityAndCollapse(intent);
            }
            record("volume panel: settings panel opened");
            return true;
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not open the volume panel", e);
            record("volume panel: could not open the settings panel ("
                    + e.getClass().getSimpleName() + ")");
            return false;
        }
    }

    /**
     * Last resort for builds without the settings volume panel: ask the audio service to show its
     * own UI. This only works while the app is in the foreground, but it is better than nothing.
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
