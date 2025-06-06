package com.flxholle.quicktiles.tiles;

import android.service.quicksettings.Tile;

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
        Tile tile = getQsTile();
        tile.setLabel("" + SharedPreferencesUtil.getCounter(this));
        tile.updateTile();
    }

    @Override
    public void reset() {
        SharedPreferencesUtil.resetCounter(this);
    }
}
