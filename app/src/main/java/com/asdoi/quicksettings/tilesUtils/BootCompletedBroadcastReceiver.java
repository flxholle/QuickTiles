package com.asdoi.quicksettings.tilesUtils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.asdoi.quicksettings.tiles.AdaptiveBrightnessTileService;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                AdaptiveBrightnessTileService.requestListeningState(context, new ComponentName(context, AdaptiveBrightnessTileService.class));
            }
        }
    }
}
