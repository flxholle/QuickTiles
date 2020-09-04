package com.asdoi.quicksettings.tiles;

import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.asdoi.quicksettings.Utils.GrayscaleServiceUtil;

public class GrayscaleService extends TileService {

    @Override
    public void onClick() {
        super.onClick();

        if (!GrayscaleServiceUtil.hasPermission(this)) {
            showDialog(GrayscaleServiceUtil.createTipsDialog(this));
            return;
        }

        int oldState = getQsTile().getState();
        if (oldState == Tile.STATE_ACTIVE) {
            setState(Tile.STATE_INACTIVE);
        } else {
            setState(Tile.STATE_ACTIVE);
        }

        GrayscaleServiceUtil.toggleGreyscale(this, oldState == Tile.STATE_INACTIVE);
    }


    private void setState(int state) {
        Tile tile = getQsTile();
        tile.setState(state);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        boolean greyscaleEnable = GrayscaleServiceUtil.isGreyscaleEnable(this);
        setState(greyscaleEnable ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
    }
}
