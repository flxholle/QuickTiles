package com.asdoi.quicksettings.intent_tiles;

import android.app.AppOpsManager;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.RemoteException;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.util.Log;

import com.asdoi.quicksettings.abstract_tiles.IntentTileService;

public class OpenDataUsageTileService extends IntentTileService {

    @Override
    public Intent createIntent() {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            intent = new Intent(Settings.ACTION_DATA_USAGE_SETTINGS);
        } else {
            intent = new Intent();
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
        }
        return intent;
    }

    @Override
    public void onStartListening() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode != AppOpsManager.MODE_ALLOWED) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }


        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getBaseContext().getSystemService(Context.NETWORK_STATS_SERVICE);

        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE, null, 0, System.currentTimeMillis());
        } catch (RemoteException e) {
            return;
        }
        Log.d("Data", bucket.getRxBytes() + "");
        Log.d("Data", bucket.getTxBytes() + "");
        long dataBytes = bucket.getTxBytes();

        //The 1000 is purpose, as Android somehow displays higher values than the ones divided with 1024
        float dataMB = Math.round((dataBytes / (1000F * 1000F)) * 100F) / 100F;
        float dataGB = Math.round((dataBytes / (1000F * 1000F * 1000F)) * 100F) / 100F;
        String txt;
        if (dataGB < 1)
            txt = dataMB + " MB";
        else
            txt = dataGB + " GB";
        Log.d("Data", txt);

        Tile tile = getQsTile();
        tile.setLabel(txt);
        tile.updateTile();
    }
}
