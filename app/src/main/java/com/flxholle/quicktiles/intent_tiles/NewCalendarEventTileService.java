package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.provider.CalendarContract;

import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

public class NewCalendarEventTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis());
        return intent;
    }
}
