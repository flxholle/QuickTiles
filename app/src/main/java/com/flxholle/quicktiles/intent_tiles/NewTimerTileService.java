package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.provider.AlarmClock;

import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

public class NewTimerTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(AlarmClock.ACTION_SET_TIMER);
    }
}
