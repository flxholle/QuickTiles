package com.flxholle.quicktiles.abstract_tiles;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public abstract class BaseTileService extends TileService {

    //This should be called when the Tile gets disabled by the user
    public abstract void reset();

    // Called when the user adds your tile.
    @Override
    public void onTileAdded() {
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
        super.onTileAdded();
    }

    // Called when your app can update your tile.
    @Override
    public void onStartListening() {
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
        super.onStartListening();
    }
}
