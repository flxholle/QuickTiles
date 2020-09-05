package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.AlarmClock;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class NewTimerTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(AlarmClock.ACTION_SET_TIMER);
    }
}
