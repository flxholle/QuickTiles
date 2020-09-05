package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.AlarmClock;

import com.asdoi.quicksettings.utils.IntentTileService;

public class NewTimerTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(AlarmClock.ACTION_SET_TIMER);
    }
}
