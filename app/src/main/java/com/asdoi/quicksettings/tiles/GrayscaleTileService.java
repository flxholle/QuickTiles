package com.asdoi.quicksettings.tiles;

import android.service.quicksettings.Tile;

import com.asdoi.quicksettings.utils.BaseTileService;
import com.asdoi.quicksettings.utils.GrantPermissionDialogs;
import com.asdoi.quicksettings.utils.SettingsUtils;

public class GrayscaleTileService extends BaseTileService {

    @Override
    public void onClick() {
        super.onClick();

        if (!GrantPermissionDialogs.hasWriteSecureSettingsPermission(this)) {
            showDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this));
            return;
        }

        int oldState = getQsTile().getState();
        if (oldState == Tile.STATE_ACTIVE) {
            setState(Tile.STATE_INACTIVE);
        } else {
            setState(Tile.STATE_ACTIVE);
        }

        SettingsUtils.toggleGreyscale(this, oldState == Tile.STATE_INACTIVE);
    }

    private void setState(int state) {
        Tile tile = getQsTile();
        tile.setState(state);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        boolean greyscaleEnable = SettingsUtils.isGreyscaleEnable(this);
        setState(greyscaleEnable ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
    }

    @Override
    public void reset() {
        SettingsUtils.toggleGreyscale(this, false);
    }
}
