package com.asdoi.quicksettings.tiles;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.service.quicksettings.Tile;

import androidx.appcompat.app.AlertDialog;

import com.asdoi.quicksettings.R;
import com.asdoi.quicksettings.tilesUtils.AdaptiveBrightnessTileJobService;
import com.asdoi.quicksettings.utils.BaseTileService;
import com.asdoi.quicksettings.utils.GrantPermissionDialogs;

public class AdaptiveBrightnessService extends BaseTileService {

    public static void disableBrightnessMode(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    @Override
    public void onCreate() {
        requestListeningState(this, new ComponentName(this, getClass()));
        AdaptiveBrightnessTileJobService.scheduleUpdateJob(this);
        super.onCreate();
    }

    @Override
    public void onStartListening() {
        updateTileResources();
        super.onStartListening();
    }

    @Override
    public void onTileRemoved() {
        AdaptiveBrightnessTileJobService.cancelJob(this);
        super.onTileRemoved();
    }

    @Override
    public void onClick() {
        if (GrantPermissionDialogs.hasModifySystemSettingsPermission(this)) {
            changeBrightnessMode();
        } else
            showDialog(GrantPermissionDialogs.getModifySystemSettingsDialog(this));
        super.onClick();
    }

    private void updateTileResources() {
        if (this.getQsTile() != null) {
            Tile tile = this.getQsTile();
            tile.setLabel(getString(R.string.adaptive_brightness));
            try {
                if (Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                    tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_brightness_auto));
                    tile.setState(Tile.STATE_ACTIVE);
                } else {
                    tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_brightness_auto_off));
                    tile.setState(Tile.STATE_INACTIVE);
                }
            } catch (Settings.SettingNotFoundException e) {
                tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_brightness_auto));
                tile.setState(Tile.STATE_INACTIVE);
            }
            tile.updateTile();
        }
    }

    private void changeBrightnessMode() {
        try {
            Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE)
                            == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC ?
                            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL :
                            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } catch (Settings.SettingNotFoundException e) {
            showSettingNotFoundDialog();
        }
    }

    private void showSettingNotFoundDialog() {
        new AlertDialog.Builder(this).setCancelable(true)
                .setTitle(R.string.require_permission)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .setMessage(R.string.modify_system_settings_not_found)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void reset() {
        disableBrightnessMode(this);
    }
}