package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.provider.Settings;

import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

public class OpenHomeTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Settings.ACTION_HOME_SETTINGS);
    }
}
