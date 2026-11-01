package com.flxholle.quicktiles;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;

import com.flxholle.quicktiles.utils.TileDiagnostics;

/**
 * Invisible activity whose only job is to make the system show its <b>compact</b> volume panel -
 * the short media volume slider that appears when a volume key is pressed, not the big panel with
 * ring, notification, alarm and call volume.
 * <p>
 * The panel cannot be requested from the tile service directly: SystemUI only honours
 * {@link AudioManager#FLAG_SHOW_UI} for apps that are in the foreground, so the request made from a
 * background service is dropped without an error. Being a real (transparent, instantly finishing)
 * activity puts QuickTiles in the foreground for exactly that one call.
 */
public class VolumePanelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCompactVolumePanel();
        finish();
        overridePendingTransition(0, 0);
    }

    private void showCompactVolumePanel() {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioManager == null) {
            TileDiagnostics.record(this, "volume panel: no AudioManager");
            return;
        }
        try {
            audioManager.adjustStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    AudioManager.ADJUST_SAME,
                    AudioManager.FLAG_SHOW_UI);
            TileDiagnostics.record(this, "volume panel: compact panel requested");
        } catch (Exception e) {
            TileDiagnostics.recordError(this, "volume panel: could not show the compact panel", e);
        }
    }
}
