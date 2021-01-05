package com.asdoi.quicksettings.intent_tiles;

import android.content.Intent;
import android.provider.Settings;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class OpenDeveloperOptionsTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
    }
}
