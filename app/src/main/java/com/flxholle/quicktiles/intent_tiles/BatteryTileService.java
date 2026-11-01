package com.flxholle.quicktiles.intent_tiles;

import android.content.Intent;
import android.os.BatteryManager;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.flxholle.quicktiles.abstract_tiles.IntentTileService;

public class BatteryTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        return new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
    }

    @Override
    public void onStartListening() {
        Tile tile = getTileOrNull();
        if (tile == null) {
            return;
        }
        try {
            BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            tile.setLabel(batLevel + " %");
            tile.updateTile();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not update battery tile", e);
        }
    }
}
