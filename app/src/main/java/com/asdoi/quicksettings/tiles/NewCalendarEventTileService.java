package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.CalendarContract;

import com.asdoi.quicksettings.utils.BaseTileService;

public class NewCalendarEventTileService extends BaseTileService {

    @Override
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, System.currentTimeMillis());
        startActivity(intent);
        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void reset() {
    }
}
