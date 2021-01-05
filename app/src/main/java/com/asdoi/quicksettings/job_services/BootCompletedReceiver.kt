package com.asdoi.quicksettings.job_services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action)
            ScreenTimeoutJobService.scheduleJob(context!!)
    }
}