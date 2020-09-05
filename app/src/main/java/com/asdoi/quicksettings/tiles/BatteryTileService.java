package com.asdoi.quicksettings.tiles;

import android.content.Intent;
import android.os.BatteryManager;
import android.service.quicksettings.Tile;

import com.asdoi.quicksettings.utils.BaseTileService;

public class BatteryTileService extends BaseTileService {

    @Override
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void onStartListening() {
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Tile tile = getQsTile();
        tile.setLabel(batLevel + " %");
        tile.updateTile();
    }

    @Override
    public void reset() {
    }
}
