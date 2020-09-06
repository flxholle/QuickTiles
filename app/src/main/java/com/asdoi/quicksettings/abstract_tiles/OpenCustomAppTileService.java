package com.asdoi.quicksettings.abstract_tiles;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;

import com.asdoi.quicksettings.R;
import com.asdoi.quicksettings.utils.SelectApp;
import com.asdoi.quicksettings.utils.SharedPreferencesUtil;

public abstract class OpenCustomAppTileService extends IntentTileService {

    @Override
    public void onStartListening() {
        updateState();
    }

    @Override
    public Intent createIntent() {
        updateState();
        String packageName = SharedPreferencesUtil.getCustomPackage(this, getPreferencesKey());
        if (packageName != null)
            return getPackageManager().getLaunchIntentForPackage(packageName);
        else {
            showDialog(SelectApp.selectApps(this, getPreferencesKey()));
            return getPackageManager().getLaunchIntentForPackage(getApplicationInfo().packageName);
        }
    }

    private void updateState() {
        Tile tile = getQsTile();
        String packageName = SharedPreferencesUtil.getCustomPackage(this, getPreferencesKey());
        if (packageName != null) {
            ApplicationInfo selectedApp = SelectApp.getApplicationInfo(this, packageName);
            tile.setLabel(selectedApp.loadLabel(getPackageManager()));
        } else {
            tile.setLabel(getString(R.string.custom_app));
            tile.setIcon(Icon.createWithResource(this, R.drawable.ic_apps));
        }
        tile.updateTile();
    }

    public abstract String getPreferencesKey();
}
