package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.provider.AlarmClock;

import com.asdoi.quicksettings.utils.BaseTileService;

public class NewAlarmTileService extends BaseTileService {

    @Override
    public void onClick() {
        Intent add_alarm = new Intent(AlarmClock.ACTION_SET_ALARM);
        add_alarm.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
        add_alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(add_alarm);
        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void reset() {
    }
}
