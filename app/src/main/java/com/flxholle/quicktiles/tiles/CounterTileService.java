package com.flxholle.quicktiles.tiles;

import android.service.quicksettings.Tile;
import android.util.Log;

import com.flxholle.quicktiles.abstract_tiles.BaseTileService;
import com.flxholle.quicktiles.utils.SharedPreferencesUtil;

public class CounterTileService extends BaseTileService {

    @Override
    public void onStartListening() {
        updateTileResources();
    }

    @Override
    public void onClick() {
        SharedPreferencesUtil.triggerCounter(this);
        updateTileResources();
    }

    private void updateTileResources() {
        Tile tile = getTileOrNull();
        if (tile == null) {
            return;
        }
        try {
            tile.setLabel("" + SharedPreferencesUtil.getCounter(this));
            tile.updateTile();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not update counter tile", e);
        }
    }

    @Override
    public void reset() {
        SharedPreferencesUtil.resetCounter(this);
    }
}
