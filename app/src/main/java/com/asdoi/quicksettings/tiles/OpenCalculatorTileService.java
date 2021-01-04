package com.asdoi.quicksettings.tiles;

import android.content.Intent;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class OpenCalculatorTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_CALCULATOR);
    }
}
