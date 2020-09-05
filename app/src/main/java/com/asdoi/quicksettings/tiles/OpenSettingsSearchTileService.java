package com.asdoi.quicksettings.tiles;

import android.content.ComponentName;
import android.content.Intent;

import com.asdoi.quicksettings.utils.IntentTileService;

public class OpenSettingsSearchTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings.intelligence", "com.android.settings.intelligence.search.SearchActivity"));
        return intent;
    }
}
