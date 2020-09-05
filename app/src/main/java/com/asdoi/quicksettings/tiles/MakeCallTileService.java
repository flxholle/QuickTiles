package com.asdoi.quicksettings.tiles;

import android.content.Intent;

import com.asdoi.quicksettings.utils.BaseTileService;

public class MakeCallTileService extends BaseTileService {

    @Override
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void reset() {
    }
}
