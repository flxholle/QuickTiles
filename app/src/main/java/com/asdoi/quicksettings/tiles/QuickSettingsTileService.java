package com.asdoi.quicksettings.tiles;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.asdoi.quicksettings.SettingsActivity;

public abstract class QuickSettingsTileService extends TileService {
    public void onStartListening() {
        Tile tile = getQsTile();
        if (SettingsActivity.isTileEnabled(this, getClass()))
            tile.setState(Tile.STATE_INACTIVE);
        else
            tile.setState(Tile.STATE_UNAVAILABLE);
        tile.updateTile();
    }
}
