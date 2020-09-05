package com.asdoi.quicksettings.tiles;

import android.content.Intent;

import com.asdoi.quicksettings.utils.IntentTileService;

public class MakeCallTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Intent.ACTION_DIAL);
    }
}
