package com.flxholle.quicktiles.abstract_tiles;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.flxholle.quicktiles.utils.TileDiagnostics;

public abstract class BaseTileService extends TileService {

    public static final String LOG_TAG = "QuickTiles";

    //This should be called when the Tile gets disabled by the user
    public abstract void reset();

    // Called when the user taps the tile
    protected abstract void onTileClicked();

    @Override
    public void onCreate() {
        super.onCreate();
        record("onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        record("onBind");
        return super.onBind(intent);
    }

    @Override
    public void onDestroy() {
        record("onDestroy");
        super.onDestroy();
    }

    // Called when the user taps the tile. This is final on purpose: every click is logged and
    // exceptions are caught here, so a failing tile can never take the whole process (and with it
    // every other tile of the app) down.
    @Override
    public final void onClick() {
        record("onClick");
        try {
            onTileClicked();
        } catch (Throwable error) {
            TileDiagnostics.recordError(this, getClass().getSimpleName() + " click failed", error);
            showErrorToast(error);
        }
    }

    // Called when the user adds your tile.
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        record("onTileAdded");
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
        record("onStartListening");
        updateTileState(Tile.STATE_INACTIVE);
    }

    @Override
    public void onStopListening() {
        record("onStopListening");
        super.onStopListening();
    }

    /**
     * Shows a short message when a tap on the tile failed. Without it a failing tile looks
     * exactly like a tile the system never called, which makes the problem impossible to
     * report.
     */
    private void showErrorToast(Throwable error) {
        try {
            String detail = error.getMessage() == null
                    ? error.getClass().getSimpleName()
                    : error.getClass().getSimpleName() + ": " + error.getMessage();
            if (detail.length() > 120) {
                detail = detail.substring(0, 120);
            }
            Toast.makeText(getApplicationContext(), "QuickTiles: " + detail, Toast.LENGTH_LONG).show();
        } catch (Throwable ignored) {
            // A toast is a nice to have, never let it break the tile
        }
    }

    /**
     * Adds an entry to the tile diagnostics log shown in the app's settings.
     */
    protected final void record(String event) {
        TileDiagnostics.record(this, getClass().getSimpleName() + " " + event);
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
            record("could not show dialog: " + e.getClass().getSimpleName());
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

        record("device is locked, waiting for unlock");
        try {
            unlockAndRun(action);
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not unlock the device, running action anyway", e);
            action.run();
        }
    }
}
