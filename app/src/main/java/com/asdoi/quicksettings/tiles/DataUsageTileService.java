package com.asdoi.quicksettings.tiles;

import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.service.quicksettings.Tile;
import android.telephony.TelephonyManager;

import com.asdoi.quicksettings.utils.BaseTileService;
import com.asdoi.quicksettings.utils.GrantPermissionDialogs;

//https://github.com/RobertZagorski/NetworkStats
//https://stackoverflow.com/questions/39314314/how-to-get-current-network-usage-of-app-or-in-total-even-on-android-n/39412045#39412045
public class DataUsageTileService extends BaseTileService {

    @Override
    public void onClick() {
        if (!GrantPermissionDialogs.hasModifySystemSettingsPermission(this)) {
            showDialog(GrantPermissionDialogs.getModifySystemSettingsDialog(this));
            return;
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Intent close_notification_bar = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        sendBroadcast(close_notification_bar);
    }

    @Override
    public void onStartListening() {
        if (!GrantPermissionDialogs.hasModifySystemSettingsPermission(this))
            return;

        double totalMB = getAllTxBytesMobile() / (1024d * 1024d);
        double totalMBRounded = ((int) Math.round(totalMB * 100)) / 100d;

        Tile tile = getQsTile();
        tile.setLabel(totalMBRounded + " MB");
        tile.updateTile();
    }

    public long getAllTxBytesMobile() {
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStats.Bucket bucket;
        try {
            bucket = networkStatsManager.querySummaryForDevice(ConnectivityManager.TYPE_MOBILE,
                    getSubscriberId(this, ConnectivityManager.TYPE_MOBILE),
                    0,
                    System.currentTimeMillis());
        } catch (RemoteException e) {
            return -1;
        }
        return bucket.getTxBytes();
    }

    //Here Manifest.permission.READ_PHONE_STATS is needed
    private String getSubscriberId(Context context, int networkType) {
        if (ConnectivityManager.TYPE_MOBILE == networkType) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                return tm.getSubscriberId();
            } else
                return null;
        }
        return "";
    }


    @Override
    public void reset() {
    }
}
