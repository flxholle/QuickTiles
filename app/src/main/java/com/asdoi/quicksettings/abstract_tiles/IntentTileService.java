package com.asdoi.quicksettings.abstract_tiles;

import android.content.Intent;
import android.widget.Toast;

import com.asdoi.quicksettings.R;

public abstract class IntentTileService extends BaseTileService {

    @Override
    public final void onClick() {
        Intent intent = createIntent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityAndCollapse(intent);
        } else {
            Toast.makeText(this, R.string.no_app_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public final void reset() {
    }

    public abstract Intent createIntent();
}
