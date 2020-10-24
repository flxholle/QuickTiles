package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.Settings;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class OpenDisplayTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Settings.ACTION_DISPLAY_SETTINGS);
    }
}
