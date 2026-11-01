package com.flxholle.quicktiles.abstract_tiles;

import android.app.Dialog;
import android.content.ComponentName;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.annotation.Nullable;

public abstract class BaseTileService extends TileService {

    public static final String LOG_TAG = "QuickTiles";

    //This should be called when the Tile gets disabled by the user
    public abstract void reset();

    // Called when the user adds your tile.
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        try {
            TileService.requestListeningState(this, new ComponentName(this, this.getClass()));
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not request listening state for " + getClass().getSimpleName(), e);
        }
        updateTileState(Tile.STATE_INACTIVE);
    }

    // Called when your app can update your tile.
    @Override
    public void onStartListening() {
        super.onStartListening();
        updateTileState(Tile.STATE_INACTIVE);
    }

    /**
     * Returns the tile this service is bound to, or {@code null} if there is currently no tile.
     * <p>
     * SystemUI is free to bind a {@link TileService} without handing out a tile, and on
     * Android 16/17 this happens more often than it used to (for example while the Quick
     * Settings panel is being re-inflated or right after the process was re-created for a
     * click). Calling {@code getQsTile()} unconditionally and dereferencing the result ends
     * with a NullPointerException inside the service, which kills the app process and leaves
     * the tile unresponsive until the app is opened again. Always use this method instead.
     */
    @Nullable
    protected final Tile getTileOrNull() {
        try {
            return getQsTile();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Tile is not available for " + getClass().getSimpleName(), e);
            return null;
        }
    }

    /**
     * Sets the given state on the current tile, if any. Never throws.
     */
    protected final void updateTileState(int state) {
        Tile tile = getTileOrNull();
        if (tile == null) {
            Log.d(LOG_TAG, "No tile bound to " + getClass().getSimpleName() + ", skipping update");
            return;
        }
        try {
            tile.setState(state);
            tile.updateTile();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not update tile of " + getClass().getSimpleName(), e);
        }
    }

    /**
     * Shows a dialog above the Quick Settings panel. {@link TileService#showDialog(Dialog)} fails
     * when the service is not bound to a tile, so the failure is caught instead of crashing the
     * whole app process (which would make every other tile of the app unusable as well).
     */
    protected final void showPermissionDialog(Dialog dialog) {
        if (dialog == null) {
            return;
        }
        try {
            showDialog(dialog);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not show dialog for " + getClass().getSimpleName(), e);
        }
    }

    /**
     * Runs the given action as soon as the device is unlocked.
     * <p>
     * {@link TileService#unlockAndRun(Runnable)} is only used when the device is actually
     * locked: its keyguard callback is not guaranteed to be invoked on every Android version
     * or OEM build (and it is not invoked at all when no secure keyguard is set up), which
     * makes the tile silently do nothing. Running the action directly whenever possible keeps
     * the tiles working without relying on that callback.
     */
    protected final void runWhenUnlocked(Runnable action) {
        boolean locked;
        try {
            locked = isLocked();
        } catch (Exception e) {
            locked = false;
        }

        if (!locked) {
            action.run();
            return;
        }

        try {
            unlockAndRun(action);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not unlock the device, running action anyway", e);
            action.run();
        }
    }
}
