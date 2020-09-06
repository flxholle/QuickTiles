package com.asdoi.quicksettings.tiles;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class OpenDataUsageTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            intent = new Intent(Settings.ACTION_DATA_USAGE_SETTINGS);
        } else {
            intent = new Intent();
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
        }
        return intent;
    }
}
