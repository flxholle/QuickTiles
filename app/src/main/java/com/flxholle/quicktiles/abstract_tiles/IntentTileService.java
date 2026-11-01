package com.flxholle.quicktiles.abstract_tiles;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.flxholle.quicktiles.R;

public abstract class IntentTileService extends BaseTileService {

    @Override
    public final void onClick() {
        Intent intent = createIntent();
        if (intent == null) {
            Log.w(LOG_TAG, "No intent configured for " + getClass().getSimpleName());
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show();
            return;
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show();
            return;
        }

        runWhenUnlocked(() -> startActivityFromTile(intent));
    }

    /**
     * Starts the activity of the tile.
     * <p>
     * Android 14 forbids {@code startActivityAndCollapse(Intent)} and Android 17 additionally
     * restricts background activity starts, so the PendingIntent variant is used on every
     * supported version that has it. Older versions keep the Intent variant.
     */
    private void startActivityFromTile(Intent intent) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this,
                        getClass().getName().hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
                startActivityAndCollapse(pendingIntent);
            } else {
                startActivityAndCollapse(intent);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Could not start activity of " + getClass().getSimpleName(), e);
            Toast.makeText(this, R.string.an_error_occurred, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public final void reset() {
    }

    public abstract Intent createIntent();
}
