package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.Settings;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class OpenAboutPhoneTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Settings.ACTION_DEVICE_INFO_SETTINGS);
//        Settings.ACTION_INPUT_METHOD_SETTINGS;
//        Settings.ACTION_LOCALE_SETTINGS;
    }
}
