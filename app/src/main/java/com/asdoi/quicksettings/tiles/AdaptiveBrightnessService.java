package com.asdoi.quicksettings.tiles;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import androidx.appcompat.app.AlertDialog;

import com.asdoi.quicksettings.R;
import com.asdoi.quicksettings.Utils.AdaptiveBrightnessTileJobService;

public class AdaptiveBrightnessService extends TileService {

    public static final int PERMISSION_DIALOG = 42;
    private static final int SETTING_NOT_FOUND_DIALOG = 24;

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
        if (Settings.System.canWrite(this)) {
            changeBrightnessMode();
        } else {
            showDialog(this, PERMISSION_DIALOG);
        }
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
            showDialog(this, SETTING_NOT_FOUND_DIALOG);
        }
    }

    public static void disableBrightnessMode(Context context) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    public static void showDialog(final Context context, int whichDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true)
                .setIcon(R.drawable.ic_brightness_auto)
                .setTitle(R.string.require_permission)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        switch (whichDialog) {
            case PERMISSION_DIALOG:
                builder.setMessage(R.string.permission_alert_dialog_message);
                builder.setPositiveButton(R.string.settings, (dialog, which) -> {
                    context.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .setData(Uri.parse("package:" + context.getPackageName())));
                });
                break;
            case SETTING_NOT_FOUND_DIALOG:
                builder.setMessage(R.string.setting_not_found_alert_dialog_message);
                builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss());
                break;
        }
        builder.show();
    }
}