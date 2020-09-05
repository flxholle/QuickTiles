package com.asdoi.quicksettings.tiles;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.service.quicksettings.Tile;

import com.asdoi.quicksettings.abstract_tiles.BaseTileService;
import com.asdoi.quicksettings.utils.GrantPermissionDialogs;

public class GrayscaleTileService extends BaseTileService {
    private static final String DISPLAY_DALTONIZER_ENABLED = "accessibility_display_daltonizer_enabled";
    private static final String DISPLAY_DALTONIZER = "accessibility_display_daltonizer";

    public static boolean isGreyscaleEnable(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        return Settings.Secure.getInt(contentResolver, DISPLAY_DALTONIZER_ENABLED, 0) == 1
                && Settings.Secure.getInt(contentResolver, DISPLAY_DALTONIZER, 0) == 0;
    }

    public static void toggleGreyscale(Context context, boolean greyscale) {
        ContentResolver contentResolver = context.getContentResolver();
        Settings.Secure.putInt(contentResolver, DISPLAY_DALTONIZER_ENABLED, greyscale ? 1 : 0);
        Settings.Secure.putInt(contentResolver, DISPLAY_DALTONIZER, greyscale ? 0 : -1);
    }

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

        toggleGreyscale(this, oldState == Tile.STATE_INACTIVE);
    }

    private void setState(int state) {
        Tile tile = getQsTile();
        tile.setState(state);
        tile.updateTile();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        boolean greyscaleEnable = isGreyscaleEnable(this);
        setState(greyscaleEnable ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
    }

    @Override
    public void reset() {
        toggleGreyscale(this, false);
    }
}
