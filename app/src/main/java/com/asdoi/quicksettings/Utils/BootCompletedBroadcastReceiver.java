package com.asdoi.quicksettings.Utils;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.asdoi.quicksettings.tiles.AdaptiveBrightnessService;

public class BootCompletedBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
                AdaptiveBrightnessService.requestListeningState(context, new ComponentName(context, AdaptiveBrightnessService.class));
            }
        }
    }
}
