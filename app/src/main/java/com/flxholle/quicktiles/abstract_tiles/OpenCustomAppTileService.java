package com.flxholle.quicktiles.abstract_tiles;

import android.content.Intent;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.flxholle.quicktiles.R;
import com.flxholle.quicktiles.utils.SelectApp;
import com.flxholle.quicktiles.utils.SharedPreferencesUtil;

public abstract class OpenCustomAppTileService extends IntentTileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        updateState();
    }

    @Override
    public void onStartListening() {
        updateState();
    }

    @Override
    public Intent createIntent() {
        String packageName = SharedPreferencesUtil.getCustomPackage(this, getPreferencesKey());
        if (packageName != null) {
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
            if (launchIntent != null) {
                return launchIntent;
            }
            Log.w(LOG_TAG, "App " + packageName + " is not installed anymore");
        } else {
            showPermissionDialog(SelectApp.selectApps(this, getPreferencesKey()));
        }
        return getPackageManager().getLaunchIntentForPackage(getApplicationInfo().packageName);
    }

    private void updateState() {
        Tile tile = getTileOrNull();
        if (tile == null) {
            return;
        }

        try {
            String packageName = SharedPreferencesUtil.getCustomPackage(this, getPreferencesKey());
            if (packageName != null) {
                tile.setLabel(SharedPreferencesUtil.getCustomPackage(this, getLabelPreferenceKey()));
            } else {
                tile.setLabel(getString(R.string.custom_app));
            }
            tile.updateTile();
        } catch (Exception e) {
            Log.w(LOG_TAG, "Could not update tile of " + getClass().getSimpleName(), e);
        }
    }

    public abstract String getPreferencesKey();

    public abstract String getLabelPreferenceKey();
}
