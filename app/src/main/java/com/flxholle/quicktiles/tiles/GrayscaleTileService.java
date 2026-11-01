package com.flxholle.quicktiles.tiles;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.flxholle.quicktiles.abstract_tiles.BaseTileService;
import com.flxholle.quicktiles.utils.GrantPermissionDialogs;

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
    public void onTileClicked() {
        if (!GrantPermissionDialogs.hasWriteSecureSettingsPermission(this)) {
            showPermissionDialog(GrantPermissionDialogs.getWriteSecureSettingsDialog(this));
            return;
        }

        // The state is read from the system instead of from the tile, because the tile is not
        // necessarily attached when a click arrives (the service may just have been re-created).
        boolean enable = !isGreyscaleEnable(this);
        toggleGreyscale(this, enable);

        setState(enable ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
    }

    private void setState(int state) {
        Tile tile = getTileOrNull();
        if (tile == null) {
            return;
        }
        try {
            tile.setState(state);
            tile.updateTile();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not update grayscale tile", e);
        }
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
