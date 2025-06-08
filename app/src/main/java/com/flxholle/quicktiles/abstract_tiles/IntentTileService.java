package com.flxholle.quicktiles.abstract_tiles;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.flxholle.quicktiles.R;

public abstract class IntentTileService extends BaseTileService {

    @Override
    public final void onClick() {
        Intent intent = createIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) != null) {
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                startActivityAndCollapse(intent);
            } else {
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
                startActivityAndCollapse(pendingIntent);
            }
        } else {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public final void reset() {
    }

    public abstract Intent createIntent();
}
