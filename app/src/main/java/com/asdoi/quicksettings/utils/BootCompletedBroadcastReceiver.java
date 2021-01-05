package com.asdoi.quicksettings.utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.asdoi.quicksettings.job_services.AdaptiveBrightnessTileJobService;
import com.asdoi.quicksettings.tiles.AdaptiveBrightnessTileService;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            AdaptiveBrightnessTileJobService.scheduleUpdateJob(context);
            AdaptiveBrightnessTileService.requestListeningState(context, new ComponentName(context, AdaptiveBrightnessTileService.class));
        }
    }
}
