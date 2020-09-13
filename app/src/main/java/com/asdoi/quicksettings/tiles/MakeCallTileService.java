package com.asdoi.quicksettings.tiles;

import android.content.Intent;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class MakeCallTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Intent.ACTION_DIAL);
    }
}
