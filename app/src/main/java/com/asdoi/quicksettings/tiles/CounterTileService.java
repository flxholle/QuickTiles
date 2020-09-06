package com.asdoi.quicksettings.tiles;

import android.service.quicksettings.Tile;

import com.asdoi.quicksettings.abstract_tiles.BaseTileService;
import com.asdoi.quicksettings.utils.SharedPreferencesUtil;

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
        Tile tile = getQsTile();
        tile.setLabel("" + SharedPreferencesUtil.getCounter(this));
        tile.updateTile();
    }

    @Override
    public void reset() {
        SharedPreferencesUtil.resetCounter(this);
    }
}
